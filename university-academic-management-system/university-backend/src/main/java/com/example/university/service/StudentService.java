package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.university.entity.Student;
import com.example.university.mapper.StudentMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {
} 