<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span>课程列表</span>
        <div class="actions">
          <el-input v-model="searchName" placeholder="按课程名称搜索" class="search-input" clearable @clear="handleSearch"></el-input>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button type="success" @click="handleAdd">新增课程</el-button>
        </div>
      </div>
    </template>
    
    <el-table :data="courses" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="课程ID"></el-table-column>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分"></el-table-column>
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
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input v-model.number="form.credit" type="number"></el-input>
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
import { ref, onMounted, reactive, computed } from 'vue';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';

const searchName = ref('');
const courses = ref([]);
const loading = ref(true);
const pagination = reactive({ currentPage: 1, pageSize: 10, total: 0 });
const dialogVisible = ref(false);
const dialogTitle = ref('');
const formRef = ref(null);
const form = reactive({ id: null, name: '', credit: null });
const isEdit = computed(() => form.id !== null);

const rules = reactive({
  name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  credit: [
    { required: true, message: '学分不能为空', trigger: 'blur' },
    { type: 'number', message: '学分必须是数字', trigger: 'blur' }
  ]
});

const fetchCourses = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/api/courses', {
      params: { current: pagination.currentPage, size: pagination.pageSize, name: searchName.value }
    });
    courses.value = response.data.records;
    pagination.total = response.data.total;
  } catch (error) {
    ElMessage.error('获取课程列表失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => { fetchCourses(); });
const handleSearch = () => { pagination.currentPage = 1; fetchCourses(); };
const handleSizeChange = () => { fetchCourses(); };
const handleCurrentChange = () => { fetchCourses(); };

const handleAdd = () => {
  dialogTitle.value = '新增课程';
  formRef.value?.resetFields();
  Object.assign(form, { id: null, name: '', credit: null });
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  dialogTitle.value = '编辑课程';
  Object.assign(form, row);
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      const api = isEdit.value ? axios.put(`/api/courses/${form.id}`, form) : axios.post('/api/courses', form);
      try {
        await api;
        ElMessage.success(isEdit.value ? '更新成功' : '新增成功');
        dialogVisible.value = false;
        await fetchCourses();
      } catch (error) {
        ElMessage.error(isEdit.value ? '更新失败' : '新增失败');
      }
    }
  });
};

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除课程 ${row.name} 吗？`, '警告', { type: 'warning' })
    .then(async () => {
      try {
        await axios.delete(`/api/courses/${row.id}`);
        ElMessage.success('删除成功');
        await fetchCourses();
      } catch (error) {
        ElMessage.error('删除失败，可能有关联的选课或授课记录');
      }
    }).catch(() => {});
};
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.actions { display: flex; align-items: center; }
.search-input { width: 200px; margin-right: 10px; }
.pagination-block { margin-top: 20px; display: flex; justify-content: flex-end; }
</style> 