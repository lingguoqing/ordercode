<template>
  <el-container class="layout-container">
    <el-header class="header">
      <div class="logo">教务管理系统 - 管理端</div>
      <div class="user-info">
        <span>欢迎, {{ adminName }}</span>
        <el-button type="danger" plain @click="logout">退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="aside">
        <el-menu
          :default-active="$route.path"
          class="el-menu-vertical-demo"
          router
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
        >
          <el-menu-item index="/admin/students">
            <el-icon><User /></el-icon>
            <span>学生管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/teachers">
            <el-icon><Avatar /></el-icon>
            <span>教师管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/classes">
            <el-icon><School /></el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/courses">
            <el-icon><Collection /></el-icon>
            <span>课程管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/exams">
            <el-icon><Document /></el-icon>
            <span>考试管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/majors">
            <el-icon><Notebook /></el-icon>
            <span>专业管理</span>
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
import {
  User,
  Avatar,
  School,
  Collection,
  Document,
  Notebook
} from '@element-plus/icons-vue';

const router = useRouter();
const adminName = ref('Admin');

onMounted(() => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.username) {
        adminName.value = user.username;
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
  }).catch(() => {
  });
};
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.header {
  background-color: #fff;
  color: #333;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #dcdfe6;
}
.logo {
  font-size: 20px;
}
.user-info {
  display: flex;
  align-items: center;
}
.user-info span {
  margin-right: 15px;
}
.aside {
  background-color: #545c64;
}
.el-menu {
  border-right: none;
}
.main-content {
  padding: 20px;
  background-color: #f4f5f7;
}
</style>
