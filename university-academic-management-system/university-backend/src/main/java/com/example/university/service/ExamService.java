package com.example.university.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.university.dto.ExamAdminVO;
import com.example.university.dto.ExamVO;
import com.example.university.entity.Exam;
import com.example.university.mapper.ExamMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService extends ServiceImpl<ExamMapper, Exam> {

    public List<ExamVO> getExamsByStudentId(Integer studentId) {
        return baseMapper.findExamsByStudentId(studentId);
    }

    public List<ExamAdminVO> getAllExamsWithCourseName() {
        return baseMapper.findAllExamsWithCourseName();
    }
}
