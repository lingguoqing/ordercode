<template>
  <el-card>
    <template #header>
      <span>我的考试</span>
    </template>
    <el-table :data="exams" v-loading="loading" style="width: 100%">
      <el-table-column prop="courseName" label="课程名称"></el-table-column>
      <el-table-column prop="examTime" label="考试时间">
        <template #default="scope">
          {{ formatDateTime(scope.row.examTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="location" label="考试地点"></el-table-column>
    </el-table>
     <div v-if="!loading && exams.length === 0">
        <el-empty description="暂无考试安排"></el-empty>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const exams = ref([]);
const loading = ref(true);

const fetchExams = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      const response = await axios.get(`/api/exams/student/${user.id}`);
      exams.value = response.data;
    } else {
      ElMessage.error('无法获取用户信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载考试安排失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toLocaleString('zh-CN');
};

onMounted(() => {
  fetchExams();
});
</script> 