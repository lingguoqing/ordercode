<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>专业管理</span>
        <el-button type="primary" @click="openDialog(null)">新增专业</el-button>
      </div>
    </template>

    <el-table :data="majors" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="100"></el-table-column>
      <el-table-column prop="name" label="专业名称"></el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteMajor(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px" destroy-on-close>
      <el-form :model="form" ref="formRef" label-width="80px">
        <el-form-item label="专业名称" prop="name" required>
          <el-input v-model="form.name" placeholder="请输入专业名称"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';

const majors = ref([]);
const loading = ref(true);
const dialogVisible = ref(false);
const form = ref({ id: null, name: '' });
const formRef = ref(null);

const dialogTitle = computed(() => (form.value.id ? '编辑专业' : '新增专业'));

const fetchMajors = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/api/majors');
    majors.value = response.data;
  } catch (error) {
    ElMessage.error('加载专业列表失败');
  } finally {
    loading.value = false;
  }
};

const openDialog = (major) => {
  if (major) {
    form.value = { ...major };
  } else {
    form.value = { id: null, name: '' };
  }
  dialogVisible.value = true;
};

const handleSubmit = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      const payload = { ...form.value };
      try {
        if (payload.id) {
          await axios.put(`/api/majors/${payload.id}`, payload);
          ElMessage.success('更新成功');
        } else {
          await axios.post('/api/majors', payload);
          ElMessage.success('新增成功');
        }
        dialogVisible.value = false;
        fetchMajors();
      } catch (error) {
        ElMessage.error('操作失败');
      }
    }
  });
};

const deleteMajor = (id) => {
  ElMessageBox.confirm('确定要删除这个专业吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await axios.delete(`/api/majors/${id}`);
      ElMessage.success('删除成功');
      fetchMajors();
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

onMounted(() => {
  fetchMajors();
});
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 