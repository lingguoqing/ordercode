import { createRouter, createWebHistory } from 'vue-router';
import StudentManagement from '../components/StudentManagement.vue';
import Login from '../views/Login.vue';
import MainLayout from '../layouts/MainLayout.vue';
import TeacherManagement from '../components/TeacherManagement.vue';
import ClassManagement from '../components/ClassManagement.vue';
import CourseManagement from '../components/CourseManagement.vue';
import ExamManagement from '../components/ExamManagement.vue';
import MajorManagement from '../components/MajorManagement.vue';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/admin',
    component: MainLayout,
    children: [
      { path: 'students', name: 'StudentManagement', component: StudentManagement },
      { path: 'teachers', name: 'TeacherManagement', component: TeacherManagement },
      { path: 'classes', name: 'ClassManagement', component: ClassManagement },
      { path: 'courses', name: 'CourseManagement', component: CourseManagement },
      { path: 'exams', name: 'ExamManagement', component: ExamManagement },
      { path: 'majors', name: 'MajorManagement', component: MajorManagement },
      { path: '', redirect: '/admin/students' }
    ]
  },
  {
    path: '/',
    redirect: '/login'
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
