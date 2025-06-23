import { createRouter, createWebHistory } from 'vue-router';
import StudentManagement from '../components/StudentManagement.vue';
import Login from '../views/Login.vue';
import MainLayout from '../layouts/MainLayout.vue';
import TeacherManagement from '../components/TeacherManagement.vue';
import ClassManagement from '../components/ClassManagement.vue';
import CourseManagement from '../components/CourseManagement.vue';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/admin',
    name: 'Layout',
    component: MainLayout,
    meta: { requiresAuth: true },
    redirect: '/admin/students',
    children: [
      {
        path: 'students',
        name: 'StudentManagement',
        component: StudentManagement,
        meta: { title: '学生管理' }
      },
      {
        path: 'teachers',
        name: 'TeacherManagement',
        component: TeacherManagement,
        meta: { title: '教师管理' }
      },
      {
        path: 'classes',
        name: 'ClassManagement',
        component: ClassManagement,
        meta: { title: '班级管理' }
      },
      {
        path: 'courses',
        name: 'CourseManagement',
        component: CourseManagement,
        meta: { title: '课程管理' }
      }
    ]
  },
  {
    path: '/',
    redirect: to => {
      const loggedIn = localStorage.getItem('user');
      if (loggedIn) {
        return { path: '/admin/students' };
      }
      return { path: '/login' };
    },
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const loggedIn = localStorage.getItem('user');

  if (to.meta.requiresAuth && !loggedIn) {
    next('/login');
  } else {
    next();
  }
});

export default router;
