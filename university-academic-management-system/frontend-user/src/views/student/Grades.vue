<template>
  <el-card>
    <template #header>
      <span>我的成绩</span>
    </template>
    <el-table :data="grades" v-loading="loading" style="width: 100%">
      <el-table-column prop="courseName" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分"></el-table-column>
      <el-table-column prop="courseType" label="课程类型"></el-table-column>
      <el-table-column prop="score" label="成绩">
         <template #default="scope">
          <el-tag :type="scope.row.score >= 60 ? 'success' : 'danger'">
            {{ scope.row.score }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
     <div v-if="!loading && grades.length === 0">
        <el-empty description="暂无成绩数据"></el-empty>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const grades = ref([]);
const loading = ref(true);

const fetchGrades = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      const response = await axios.get(`/api/student/${user.id}/grades`);
      grades.value = response.data;
    } else {
      ElMessage.error('无法获取用户信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载成绩失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchGrades();
});
</script> 