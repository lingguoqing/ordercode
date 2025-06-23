<template>
  <el-card>
    <template #header>
      <span>个人信息</span>
    </template>
    <div v-if="teacher" class="profile-content">
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item label="教师编号">{{ teacher.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ teacher.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ teacher.gender }}</el-descriptions-item>
        <el-descriptions-item label="职称">{{ teacher.title }}</el-descriptions-item>
        <el-descriptions-item label="联系电话" :span="2">{{ teacher.phone }}</el-descriptions-item>
      </el-descriptions>
    </div>
     <div v-else-if="loading" v-loading="loading" style="height: 200px;"></div>
    <div v-else>
      <el-empty description="无法加载教师信息"></el-empty>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const teacher = ref(null);
const loading = ref(true);

const fetchTeacherInfo = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      const response = await axios.get(`/api/teachers/${user.id}`);
      teacher.value = response.data;
    } else {
      ElMessage.error('无法获取教师信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载个人信息失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchTeacherInfo();
});
</script>

<style scoped>
.profile-content {
  padding: 20px;
}
</style> 