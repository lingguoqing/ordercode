package com.example.university.controller;

import com.example.university.entity.Student;
import com.example.university.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public IPage<Student> getStudentPage(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String name) {
        Page<Student> page = new Page<>(current, size);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }
        return studentService.page(page, wrapper);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return studentService.getById(id);
    }

    @PostMapping
    public boolean createStudent(@RequestBody Student student) {
        if (!StringUtils.hasText(student.getPassword())) {
            student.setPassword("123456"); // Set default password
        }
        return studentService.save(student);
    }

    @PutMapping("/{id}")
    public boolean updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        student.setId(id);
        return studentService.updateById(student);
    }

    @DeleteMapping("/{id}")
    public boolean deleteStudent(@PathVariable Integer id) {
        return studentService.removeById(id);
    }
} 