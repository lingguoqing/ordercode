<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>学生列表</span>
        <div class="actions">
          <el-input v-model="searchName" placeholder="按姓名搜索" class="search-input" clearable
                    @clear="handleSearch"></el-input>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="success" @click="handleAdd">新增学生</el-button>
        </div>
      </div>
    </template>

    <el-table :data="students" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="学号"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="gender" label="性别"></el-table-column>
      <el-table-column prop="age" label="年龄"></el-table-column>
      <el-table-column prop="classId" label="班级ID"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
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
        <el-form-item label="性别" prop="gender">
          <el-input v-model="form.gender"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model.number="form.age" type="number"></el-input>
        </el-form-item>
        <el-form-item label="班级" prop="classId">
          <el-select v-model="form.classId" placeholder="请选择班级">
            <el-option
                v-for="item in classList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="form.password" type="password"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
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
const students = ref([]);
const classList = ref([]);
const loading = ref(true);
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});
const dialogVisible = ref(false);
const dialogTitle = ref('');
const formRef = ref(null);
const form = reactive({
  id: null,
  name: '',
  gender: '',
  age: null,
  classId: null,
  password: ''
});

const isEdit = computed(() => form.id !== null);

const rules = reactive({
  name: [{required: true, message: '请输入姓名', trigger: 'blur'}],
  age: [{type: 'number', message: '年龄必须是数字', trigger: 'blur'}],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}]
});

const fetchClasses = async () => {
  try {
    const response = await axios.get('/api/class/all');
    classList.value = response.data;
  } catch (error) {
    ElMessage.error('获取班级列表失败');
  }
};

const fetchStudents = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/api/student', {
      params: {
        current: pagination.currentPage,
        size: pagination.pageSize,
        name: searchName.value
      }
    });
    students.value = response.data.records;
    pagination.total = response.data.total;
  } catch (error) {
    ElMessage.error('获取学生列表失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchStudents();
  fetchClasses();
});

const handleAdd = () => {
  dialogTitle.value = '新增学生';
  formRef.value?.resetFields();
  Object.assign(form, {id: null, name: '', gender: '', age: null, classId: null, password: ''});
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await axios.put(`/api/student/${form.id}`, form);
          ElMessage.success('更新成功');
        } else {
          // Create
          await axios.post('/api/student', form);
          ElMessage.success('新增成功');
        }
        dialogVisible.value = false;
        await fetchStudents(); // Refresh table
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '新增失败');
        console.error(error);
      }
    }
  });
};

const handleEdit = (row) => {
  dialogTitle.value = '编辑学生';
  Object.assign(form, row);
  form.password = '';
  dialogVisible.value = true;
};

const handleDelete = (row) => {
  ElMessageBox.confirm(
      `确定要删除学生 ${row.name} 吗？`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  ).then(async () => {
    try {
      await axios.delete(`/api/student/${row.id}`);
      ElMessage.success('删除成功');
      await fetchStudents(); // Refresh table
    } catch (error) {
      ElMessage.error('删除失败');
      console.error(error);
    }
  }).catch(() => {
  });
};

const handleSizeChange = () => {
  fetchStudents();
}

const handleCurrentChange = () => {
  fetchStudents();
}

const handleSearch = () => {
  pagination.currentPage = 1;
  fetchStudents();
}
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
