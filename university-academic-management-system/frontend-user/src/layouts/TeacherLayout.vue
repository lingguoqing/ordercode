<template>
  <el-container class="layout-container">
    <el-header class="header">
      <div class="logo">教师端</div>
      <div class="user-info">
        <span>欢迎, {{ teacherName }} 老师</span>
        <el-button type="danger" plain @click="logout">退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="aside">
        <el-menu
          :default-active="$route.path"
          class="el-menu-vertical-demo"
          router
        >
          <el-menu-item index="/teacher/profile">
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="/teacher/courses">
            <span>我的授课</span>
          </el-menu-item>
           <el-menu-item index="/teacher/grading">
            <span>成绩录入</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';

const router = useRouter();
const teacherName = ref('老师');

onMounted(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.name) {
        teacherName.value = user.name;
    }
});

const logout = () => {
  ElMessageBox.confirm('您确定要退出吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    localStorage.removeItem('user');
    router.push('/login');
  });
};
</script>

<style scoped>
.layout-container { height: 100vh; }
.header { background-color: #fff; color: #333; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #dcdfe6; }
.logo { font-size: 20px; }
.user-info { display: flex; align-items: center; }
.user-info span { margin-right: 15px; }
.aside { border-right: 1px solid #dcdfe6; }
.el-menu { border-right: none; }
.main-content { padding: 20px; background-color: #f4f5f7; }
</style> 