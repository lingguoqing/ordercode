package com.example.university.controller;

import com.example.university.dto.ExamAdminVO;
import com.example.university.dto.ExamVO;
import com.example.university.entity.Exam;
import com.example.university.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    // For Admins: Get all exams with course names
    @GetMapping("/admin")
    public List<ExamAdminVO> getAllExams() {
        return examService.getAllExamsWithCourseName();
    }

    // For Students: Get exams for a specific student
    @GetMapping("/student/{studentId}")
    public List<ExamVO> getStudentExams(@PathVariable Integer studentId) {
        return examService.getExamsByStudentId(studentId);
    }

    // For Admins: Create a new exam
    @PostMapping
    public boolean createExam(@RequestBody Exam exam) {
        return examService.save(exam);
    }

    // For Admins: Update an exam
    @PutMapping("/{id}")
    public boolean updateExam(@PathVariable Integer id, @RequestBody Exam exam) {
        exam.setId(id);
        return examService.updateById(exam);
    }

    // For Admins: Delete an exam
    @DeleteMapping("/{id}")
    public boolean deleteExam(@PathVariable Integer id) {
        return examService.removeById(id);
    }
}
