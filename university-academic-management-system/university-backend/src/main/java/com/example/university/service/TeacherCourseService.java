package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.university.entity.TeacherCourse;
import java.util.List;

public interface TeacherCourseService extends IService<TeacherCourse> {
    void updateTeacherCourses(Integer teacherId, List<Integer> courseIds);
} 