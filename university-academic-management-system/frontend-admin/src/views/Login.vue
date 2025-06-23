<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>管理员登录</span>
        </div>
      </template>
      <el-form :model="form" @keyup.enter="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';

const router = useRouter();
const form = reactive({
  username: '',
  password: '',
  role: 'admin'
});
const loading = ref(false);

const handleLogin = async () => {
  loading.value = true;
  try {
    const response = await axios.post('/api/login', form);
    localStorage.setItem('user', JSON.stringify(response.data));
    ElMessage.success('登录成功');
    router.push('/admin/students');
  } catch (error) {
    ElMessage.error('用户名或密码错误');
    console.error(error);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #f0f2f5;
}
.login-card {
  width: 400px;
}
.card-header {
  text-align: center;
  font-size: 20px;
}
</style>
