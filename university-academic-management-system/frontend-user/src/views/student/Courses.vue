<template>
  <el-card>
    <template #header>
      <span>在线选课</span>
    </template>
    <el-table :data="courses" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="课程编号"></el-table-column>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分"></el-table-column>
      <el-table-column prop="type" label="课程类型"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button 
            size="small" 
            type="primary"
            @click="handleSelect(scope.row.id)"
            :disabled="scope.row.selected"
          >
            {{ scope.row.selected ? '已选择' : '选择' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const courses = ref([]);
const loading = ref(true);
const studentId = ref(null);

const fetchAvailableCourses = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      studentId.value = user.id;
      const response = await axios.get(`/api/courses/available/${studentId.value}`);
      // Add a 'selected' flag for UI state management
      courses.value = response.data.map(course => ({ ...course, selected: false }));
    } else {
      ElMessage.error('无法获取用户信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载课程列表失败');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchAvailableCourses();
});

const handleSelect = async (courseId) => {
  try {
    await axios.post('/api/student/course', {
      studentId: studentId.value,
      courseId: courseId
    });
    ElMessage.success('选课成功');
    // Find the course in the list and mark it as selected
    const course = courses.value.find(c => c.id === courseId);
    if (course) {
      course.selected = true;
    }
  } catch (error) {
    ElMessage.error('选课失败，可能您已选择该课程');
  }
};
</script> 