<template>
  <el-card>
    <template #header>
      <span>成绩录入</span>
    </template>
    
    <!-- Step 1: Course Selection -->
    <div class="selection-area">
      <el-select v-model="selectedCourse" placeholder="请选择课程" @change="handleCourseChange" value-key="id">
        <el-option
          v-for="course in teacherCourses"
          :key="course.id"
          :label="course.name"
          :value="course">
        </el-option>
      </el-select>
    </div>

    <!-- Step 2: Student List & Grading -->
    <div v-if="selectedCourse">
      <el-divider/>
      <h3>{{ selectedCourse.name }} - 学生列表</h3>
      <el-table :data="students" v-loading="loadingStudents" style="width: 100%">
        <el-table-column prop="studentId" label="学号"></el-table-column>
        <el-table-column prop="studentName" label="姓名"></el-table-column>
        <el-table-column label="成绩">
          <template #default="scope">
            <el-input-number v-model="scope.row.score" :min="0" :max="100" controls-position="right" size="small"></el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="操作">
           <template #default="scope">
            <el-button size="small" type="primary" @click="handleSaveGrade(scope.row)">保存</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
     <div v-else class="placeholder">
        <el-empty description="请先选择一门课程"></el-empty>
    </div>

  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const teacherCourses = ref([]);
const selectedCourse = ref(null);
const students = ref([]);
const loadingStudents = ref(false);
const teacherId = ref(null);
const loading = ref(false);

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (user && user.id) {
    teacherId.value = user.id;
    fetchTeacherCourses();
  }
});

const fetchTeacherCourses = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      teacherId.value = user.id;
      const response = await axios.get(`/api/teachers/${user.id}/courses`);
      teacherCourses.value = response.data;
    } else {
      ElMessage.error('无法获取教师信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载授课列表失败');
  } finally {
    loading.value = false;
  }
};

const handleCourseChange = async (course) => {
  if (!course) {
    students.value = [];
    return;
  }
  loadingStudents.value = true;
  try {
    const response = await axios.get(`/api/course/${course.id}/students`);
    students.value = response.data;
  } catch (error) {
    ElMessage.error('加载学生列表失败');
  } finally {
    loadingStudents.value = false;
  }
};

const handleSaveGrade = async (student) => {
  try {
    await axios.put('/api/student/course/grade', {
      studentId: student.studentId,
      courseId: selectedCourse.value.id,
      score: student.score
    });
    ElMessage.success(`学生 ${student.studentName} 的成绩已保存`);
  } catch (error) {
    ElMessage.error('保存失败');
  }
};
</script>

<style scoped>
.selection-area {
  margin-bottom: 20px;
}
.placeholder {
    margin-top: 20px;
}
</style> 