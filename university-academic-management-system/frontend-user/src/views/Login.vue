<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>教务系统登录</span>
        </div>
      </template>
      <el-form :model="form" @keyup.enter="handleLogin">
        <el-form-item label="身份">
          <el-select v-model="form.role" placeholder="请选择您的身份">
            <el-option label="学生" value="student"></el-option>
            <el-option label="教师" value="teacher"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" :placeholder="usernamePlaceholder"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

const router = useRouter();
const form = reactive({
  username: '',
  password: '',
  role: 'student'
});
const loading = ref(false);

const usernamePlaceholder = computed(() => {
  return form.role === 'student' ? '请输入学号' : '请输入教师编号';
});

const handleLogin = async () => {
  if (!form.username || !form.password) {
      ElMessage.warning('请输入用户名和密码');
      return;
  }
  loading.value = true;
  try {
    const response = await axios.post('/api/login', form);
    const user = response.data;
    // Add role to the stored user object, this is crucial for the router guard
    user.role = form.role; 
    localStorage.setItem('user', JSON.stringify(user));
    
    ElMessage.success('登录成功');
    
    // Redirect based on role
    if (form.role === 'student') {
        router.push('/student/profile');
    } else if (form.role === 'teacher') {
        router.push('/teacher/profile');
    }

  } catch (error) {
    ElMessage.error('用户名或密码错误');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container { display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
.login-card { width: 400px; }
.card-header { text-align: center; font-size: 20px; }
</style> 