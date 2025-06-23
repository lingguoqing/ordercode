package com.example.university.controller;

import com.example.university.dto.GradeVO;
import com.example.university.dto.StudentGradeVO;
import com.example.university.dto.UpdateGradeDTO;
import com.example.university.entity.StudentCourse;
import com.example.university.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    @GetMapping("/student/{studentId}/grades")
    public List<GradeVO> getStudentGrades(@PathVariable Integer studentId) {
        return studentCourseService.getGradesByStudentId(studentId);
    }

    @GetMapping("/course/{courseId}/students")
    public List<StudentGradeVO> getCourseStudents(@PathVariable Integer courseId) {
        return studentCourseService.getStudentsWithGradesByCourseId(courseId);
    }

    @PutMapping("/student/course/grade")
    public boolean updateGrade(@RequestBody UpdateGradeDTO updateGradeDTO) {
        return studentCourseService.updateGrade(
            updateGradeDTO.getStudentId(),
            updateGradeDTO.getCourseId(),
            updateGradeDTO.getScore()
        );
    }

    @PostMapping("/student/course")
    public boolean selectCourse(@RequestBody StudentCourse studentCourse) {
        // In a real application, more validation would be needed here.
        // The unique constraint on the DB will prevent duplicates.
        // Score is null by default, to be filled by teacher later.
        return studentCourseService.save(studentCourse);
    }
} 