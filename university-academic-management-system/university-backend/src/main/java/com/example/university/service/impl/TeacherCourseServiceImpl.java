package com.example.university.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.university.entity.TeacherCourse;
import com.example.university.mapper.TeacherCourseMapper;
import com.example.university.service.TeacherCourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherCourseServiceImpl extends ServiceImpl<TeacherCourseMapper, TeacherCourse> implements TeacherCourseService {

    @Override
    @Transactional
    public void updateTeacherCourses(Integer teacherId, List<Integer> courseIds) {
        remove(new QueryWrapper<TeacherCourse>().eq("teacher_id", teacherId));
        if (courseIds != null && !courseIds.isEmpty()) {
            List<TeacherCourse> newAssignments = courseIds.stream()
                .map(courseId -> {
                    TeacherCourse tc = new TeacherCourse();
                    tc.setTeacherId(teacherId);
                    tc.setCourseId(courseId);
                    return tc;
                })
                .collect(Collectors.toList());
            saveBatch(newAssignments);
        }
    }
} 