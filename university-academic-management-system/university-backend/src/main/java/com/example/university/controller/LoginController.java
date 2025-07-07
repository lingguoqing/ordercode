package com.example.university.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.university.dto.LoginDTO;
import com.example.university.entity.Admin;
import com.example.university.entity.Student;
import com.example.university.entity.Teacher;
import com.example.university.service.AdminService;
import com.example.university.service.StudentService;
import com.example.university.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    // In a real app, you'd inject StudentService and TeacherService here too.

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        String role = loginDTO.getRole();
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        if ("admin".equals(role)) {
            QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            Admin admin = adminService.getOne(queryWrapper);

            if (admin != null && Objects.equals(admin.getPassword(), password)) {
                // In a real app, return a JWT token here
                return ResponseEntity.ok(admin);
            }
        } else if ("student".equals(role)) {
            LambdaQueryWrapper<Student> studentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            studentLambdaQueryWrapper.eq(Student::getName,username);
            Student student = studentService.getOne(studentLambdaQueryWrapper); // username is student ID
            if (student != null && Objects.equals(student.getPassword(), password)) {
                return ResponseEntity.ok(student);
            }
        } else if ("teacher".equals(role)) {
            LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();
            teacherLambdaQueryWrapper.eq(Teacher::getName,username);
            Teacher teacher = teacherService.getOne(teacherLambdaQueryWrapper); // username is teacher ID
            if (teacher != null && Objects.equals(teacher.getPassword(), password)) {
                return ResponseEntity.ok(teacher);
            }
        }
        
        return ResponseEntity.badRequest().body("用户名或密码错误");
    }
} 