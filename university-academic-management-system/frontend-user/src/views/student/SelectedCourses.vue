<template>
  <el-card>
    <template #header>
      <span>已选课程</span>
    </template>
    <el-table :data="courses" v-loading="loading" style="width: 100%">
      <el-table-column prop="courseName" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分"></el-table-column>
      <el-table-column prop="courseType" label="课程类型"></el-table-column>
    </el-table>
     <div v-if="!loading && courses.length === 0">
        <el-empty description="暂无已选课程"></el-empty>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const courses = ref([]);
const loading = ref(true);

const fetchCourses = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      const response = await axios.get(`/api/student/${user.id}/grades`);
      courses.value = response.data;
    } else {
      ElMessage.error('无法获取用户信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载已选课程失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchCourses();
});
</script> 