<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>教师列表</span>
        <div class="actions">
          <el-input v-model="searchName" placeholder="按教师姓名搜索" class="search-input" clearable
                    @clear="handleSearch"></el-input>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="success" @click="handleAdd">新增教师</el-button>
        </div>
      </div>
    </template>

    <el-table :data="teachers" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="教师ID"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="gender" label="性别"></el-table-column>
      <el-table-column prop="title" label="职称"></el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="primary" @click="openAssignDialog(scope.row)">分配课程</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-block">
      <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="留空则不修改"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="assignDialogVisible" :title="'为 ' + currentTeacher.name + ' 分配课程'" width="800px">
      <el-transfer
          v-model="assignedCourses"
          :data="allCourses"
          :titles="['所有课程', '已分配课程']"
          :props="{ key: 'id', label: 'name' }"
          filterable
          target-order="push"
          style="width: 100%; display: flex; justify-content: center; align-items: center;"
      ></el-transfer>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="assignDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAssignSubmit">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </el-card>
</template>

<script setup>
import {ref, onMounted, reactive, computed} from 'vue';
import axios from 'axios';
import {ElMessage, ElMessageBox} from 'element-plus';

const searchName = ref('');
const teachers = ref([]);
const loading = ref(true);
const pagination = reactive({currentPage: 1, pageSize: 10, total: 0});
const dialogVisible = ref(false);
const dialogTitle = ref('');
const formRef = ref(null);
const form = reactive({id: null, name: '', password: '', gender: '', title: ''});
const isEdit = computed(() => form.id !== null);
const rules = reactive({
  name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
  gender: [{required: true, message: '请选择性别', trigger: 'change'}]
});
const assignDialogVisible = ref(false);
const currentTeacher = ref({id: null, name: ''});
const allCourses = ref([]);
const assignedCourses = ref([]);

const fetchTeachers = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/api/teachers', {
      params: {current: pagination.currentPage, size: pagination.pageSize, name: searchName.value}
    });
    teachers.value = response.data.records;
    pagination.total = response.data.total;
  } catch (error) {
    ElMessage.error('获取教师列表失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchTeachers();
});

const handleSearch = () => {
  pagination.currentPage = 1;
  fetchTeachers();
};
const handleSizeChange = () => {
  fetchTeachers();
};
const handleCurrentChange = () => {
  fetchTeachers();
};

const handleAdd = () => {
  dialogTitle.value = '新增教师';
  formRef.value?.resetFields();
  Object.assign(form, {id: null, name: '', password: '', gender: '', title: ''});
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  dialogTitle.value = '编辑教师';
  formRef.value?.resetFields();
  Object.assign(form, row, {password: ''});
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      const payload = {...form};
      if (!payload.password) delete payload.password;

      const api = isEdit.value ? axios.put(`/api/teachers/${form.id}`, payload) : axios.post('/api/teachers', payload);
      try {
        await api;
        ElMessage.success(isEdit.value ? '更新成功' : '新增成功');
        dialogVisible.value = false;
        await fetchTeachers();
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '新增失败');
      }
    }
  });
};

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除教师 ${row.name} 吗？`, '警告', {type: 'warning'})
      .then(async () => {
        try {
          await axios.delete(`/api/teachers/${row.id}`);
          ElMessage.success('删除成功');
          await fetchTeachers();
        } catch (error) {
          ElMessage.error('删除失败');
        }
      }).catch(() => {
  });
};


const openAssignDialog = async (teacher) => {
  currentTeacher.value = teacher;
  assignDialogVisible.value = true;

  try {
    const [allCoursesRes, assignedCoursesRes] = await Promise.all([
      axios.get('/api/courses/all'),
      axios.get(`/api/courses/by-teacher/${teacher.id}`)
    ]);

    allCourses.value = allCoursesRes.data;
    assignedCourses.value = assignedCoursesRes.data.map(c => c.id);

  } catch (error) {
    ElMessage.error('获取课程数据失败');
    assignDialogVisible.value = false;
  }
};

const handleAssignSubmit = async () => {
  try {
    await axios.post(`/api/teacher-courses/${currentTeacher.value.id}`, assignedCourses.value);
    ElMessage.success('课程分配成功');
    assignDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('课程分配失败');
  }
};

</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.actions {
  display: flex;
  align-items: center;
}

.search-input {
  width: 200px;
  margin-right: 10px;
}

.pagination-block {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
