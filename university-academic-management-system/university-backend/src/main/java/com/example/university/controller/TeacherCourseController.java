package com.example.university.controller;

import com.example.university.service.TeacherCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-courses")
public class TeacherCourseController {

    @Autowired
    private TeacherCourseService teacherCourseService;

    @PostMapping("/{teacherId}")
    public void updateTeacherCourses(@PathVariable Integer teacherId, @RequestBody List<Integer> courseIds) {
        teacherCourseService.updateTeacherCourses(teacherId, courseIds);
    }
} 