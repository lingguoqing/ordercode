import { createRouter, createWebHistory } from 'vue-router';
import { ElMessage } from 'element-plus';
import StudentLayout from '../layouts/StudentLayout.vue';
import TeacherLayout from '../layouts/TeacherLayout.vue';

// We will create these components in subsequent steps
const Login = () => import('../views/Login.vue');
const StudentProfile = () => import('../views/student/Profile.vue');
const StudentGrades = () => import('../views/student/Grades.vue');
const StudentCourses = () => import('../views/student/Courses.vue');
const TeacherProfile = () => import('../views/teacher/Profile.vue');
const TeacherCourses = () => import('../views/teacher/Courses.vue');
const TeacherGrading = () => import('../views/teacher/Grading.vue');

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/student',
    name: 'StudentLayout',
    component: StudentLayout,
    meta: { requiresAuth: true, role: 'student' },
    redirect: '/student/profile',
    children: [
      { path: 'profile', name: 'StudentProfile', component: StudentProfile },
      { path: 'grades', name: 'StudentGrades', component: StudentGrades },
      { path: 'courses', name: 'StudentCourses', component: StudentCourses }
      // other student routes like /student/courses can be added here
    ]
  },
  {
    path: '/teacher',
    name: 'TeacherLayout',
    component: TeacherLayout,
    meta: { requiresAuth: true, role: 'teacher' },
    redirect: '/teacher/profile',
    children: [
      { path: 'profile', name: 'TeacherProfile', component: TeacherProfile },
      { path: 'courses', name: 'TeacherCourses', component: TeacherCourses },
      { path: 'grading', name: 'TeacherGrading', component: TeacherGrading }
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
  const user = JSON.parse(localStorage.getItem('user'));

  if (to.meta.requiresAuth) {
    if (!user) {
      // Not logged in
      next('/login');
    } else if (to.meta.role && user.role !== to.meta.role) {
      // Logged in, but wrong role
      // This prevents a student from accessing a teacher's URL
      ElMessage.error('无权访问');
      next('/login');
    } else {
      // Logged in and has correct role
      next();
    }
  } else {
    next();
  }
});

export default router; 