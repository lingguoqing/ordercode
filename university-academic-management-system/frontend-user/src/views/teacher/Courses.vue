<template>
  <el-card>
    <template #header>
      <span>我的授课列表</span>
    </template>
    <el-table :data="courses" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="课程编号"></el-table-column>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分"></el-table-column>
      <el-table-column prop="type" label="课程类型"></el-table-column>
    </el-table>
    <div v-if="!loading && courses.length === 0">
        <el-empty description="暂无授课数据"></el-empty>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const courses = ref([]);
const loading = ref(true);

const fetchTeacherCourses = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      const response = await axios.get(`/api/teachers/${user.id}/courses`);
      courses.value = response.data;
    } else {
      ElMessage.error('无法获取教师信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载授课列表失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchTeacherCourses();
});
</script> 