<template>
  <el-card>
    <template #header>
      <span>我的课表</span>
    </template>
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>
    <div v-else-if="schedule.length === 0" class="empty-container">
      <el-empty description="暂无课表信息"></el-empty>
    </div>
    <div v-else class="schedule-grid">
      <div class="header" v-for="day in days" :key="day">{{ day }}</div>
      <div class="time-slots">
        <div v-for="n in 12" :key="n" class="time-slot-label">{{ n }}</div>
      </div>
      <div class="grid-body">
        <template v-for="(course, index) in processedCourses" :key="index">
          <div :style="course.style" class="course-item">
            <div class="course-name">{{ course.courseName }}</div>
            <div class="teacher-name">{{ course.teacherName }}</div>
            <div class="location">{{ course.location }}</div>
          </div>
        </template>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const schedule = ref([]);
const loading = ref(true);
const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
const dayMapping = { 'Mon': 1, 'Tue': 2, 'Wed': 3, 'Thu': 4, 'Fri': 5, 'Sat': 6, 'Sun': 7 };

const fetchSchedule = async () => {
  loading.value = true;
  try {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.id) {
      const response = await axios.get(`/api/student/${user.id}/schedule`);
      schedule.value = response.data;
    } else {
      ElMessage.error('无法获取用户信息，请重新登录');
    }
  } catch (error) {
    ElMessage.error('加载课表失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

const processedCourses = computed(() => {
  const result = [];
  schedule.value.forEach(course => {
    if (!course.classTime) return;
    // Example format: "Mon 1-2, Wed 3-4"
    const parts = course.classTime.split(',');
    parts.forEach(part => {
      const trimmedPart = part.trim();
      const match = trimmedPart.match(/(\w+)\s*(\d+)-(\d+)/);
      if (match) {
        const dayAbbr = match[1];
        const start = parseInt(match[2], 10);
        const end = parseInt(match[3], 10);
        const dayIndex = dayMapping[dayAbbr];

        if (dayIndex) {
          result.push({
            ...course,
            style: {
              gridColumn: dayIndex,
              gridRow: `${start} / ${end + 1}`,
              backgroundColor: getRandomColor()
            }
          });
        }
      }
    });
  });
  return result;
});

// Helper to get random colors for courses
const getRandomColor = () => {
  const letters = '89ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * letters.length)];
  }
  return color;
}

onMounted(() => {
  fetchSchedule();
});
</script>

<style scoped>
.schedule-grid {
  display: grid;
  grid-template-columns: 60px repeat(7, 1fr);
  grid-template-rows: auto;
  gap: 2px;
  background-color: #e9eef3;
}
.header {
  background-color: #f5f7fa;
  text-align: center;
  padding: 10px;
  font-weight: bold;
}
.time-slots {
  grid-column: 1 / 2;
  display: grid;
  grid-template-rows: repeat(12, 60px);
}
.time-slot-label {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  border-top: 1px solid #e9eef3;
  font-size: 14px;
}
.grid-body {
  grid-column: 2 / 9;
  grid-row: 2 / 3;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-template-rows: repeat(12, 60px);
  gap: 2px;
}
.course-item {
  border-radius: 4px;
  padding: 5px;
  color: #fff;
  font-size: 12px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}
.course-name { font-weight: bold; }
</style> 