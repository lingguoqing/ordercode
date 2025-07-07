<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>考试安排管理</span>
        <el-button type="primary" @click="openDialog(null)">新增考试安排</el-button>
      </div>
    </template>

    <el-table :data="exams" v-loading="loading" style="width: 100%">
      <el-table-column prop="courseName" label="课程名称"></el-table-column>
      <el-table-column prop="examTime" label="考试时间">
        <template #default="scope">
          {{ formatDateTime(scope.row.examTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="location" label="考试地点"></el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="openDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteExam(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close>
      <el-form :model="form" ref="formRef" label-width="100px">
        <el-form-item label="选择课程" prop="courseId" required>
           <el-select v-model="form.courseId" placeholder="请选择课程" filterable style="width: 100%">
                <el-option v-for="course in validCourses" :key="course.id" :label="course.name" :value="course.id"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="考试时间" prop="examTime" required>
          <el-date-picker
            v-model="form.examTime"
            type="datetime"
            placeholder="选择日期时间"
            style="width: 100%"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="考试地点" prop="location" required>
          <el-input v-model="form.location" placeholder="请输入考试地点"></el-input>
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

const exams = ref([]);
const courses = ref([]);
const loading = ref(true);
const dialogVisible = ref(false);
const form = ref({
  id: null,
  courseId: '',
  examTime: '',
  location: ''
});
const formRef = ref(null);

const validCourses = computed(() => {
  if (!Array.isArray(courses.value)) {
    return [];
  }
  return courses.value.filter(c => c && c.id);
});

const dialogTitle = computed(() => (form.value.id ? '编辑考试安排' : '新增考试安排'));

const fetchExams = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/api/exams/admin');
    exams.value = response.data;
  } catch (error) {
    ElMessage.error('加载考试列表失败');
  } finally {
    loading.value = false;
  }
};

const fetchCourses = async () => {
    try {
        const response = await axios.get('/api/courses/all');
        courses.value = response.data;
    } catch (error) {
        ElMessage.error('加载课程列表失败');
    }
}

const openDialog = (exam) => {
  if (exam) {
    // Edit mode
    form.value = { ...exam };
  } else {
    // Add mode
    form.value = { id: null, courseId: null, examTime: '', location: '' };
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
          await axios.put(`/api/exams/${payload.id}`, payload);
          ElMessage.success('更新成功');
        } else {
          await axios.post('/api/exams', payload);
          ElMessage.success('新增成功');
        }
        dialogVisible.value = false;
        fetchExams();
      } catch (error) {
        ElMessage.error('操作失败');
      }
    }
  });
};

const deleteExam = (id) => {
  ElMessageBox.confirm('确定要删除这个考试安排吗?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await axios.delete(`/api/exams/${id}`);
      ElMessage.success('删除成功');
      fetchExams();
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

const formatDateTime = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toLocaleString('zh-CN');
};

onMounted(() => {
  fetchExams();
  fetchCourses();
});
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 