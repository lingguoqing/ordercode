package com.example.university.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.university.entity.Course;
import com.example.university.mapper.CourseMapper;
import com.example.university.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public List<Course> findAvailableCoursesForStudent(Integer studentId) {
        return baseMapper.findAvailableCoursesForStudent(studentId);
    }

    @Override
    public List<Course> findCoursesByTeacherId(Integer teacherId) {
        return baseMapper.findCoursesByTeacherId(teacherId);
    }
} 