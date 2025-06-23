package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.university.entity.Course;
import java.util.List;

public interface CourseService extends IService<Course> {
    List<Course> findAvailableCoursesForStudent(Integer studentId);
    List<Course> findCoursesByTeacherId(Integer teacherId);
} 