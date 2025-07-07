<template>
  <el-card>
    <template #header>
      <span>个人信息</span>
    </template>
    <div v-if="student" class="profile-content">
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item label="学号">{{ student.id }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ student.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ student.gender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ student.age }}</el-descriptions-item>
        <el-descriptions-item label="入学日期" :span="2">{{ formatDate(student.enrollmentDate) }}</el-descriptions-item>
        <el-descriptions-item label="班级ID">{{ student.classId }}</el-descriptions-item>
      </el-descriptions>
    </div>
     <div v-else-if="loading" v-loading="loading" style="height: 200px;"></div>
    <div v-else>
      <el-empty description="无法加载学生信息"></el-empty>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const student = ref(null);
const loading = ref(true);

const fetchStudentProfile = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      const response = await axios.get(`/api/student/${user.id}`);
      student.value = response.data;
    } else {
      ElMessage.error('无法获取用户信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载个人信息失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

onMounted(() => {
  fetchStudentProfile();
});
</script>

<style scoped>
.profile-content {
  padding: 20px;
}
</style> 