package com.example.university.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.university.dto.GradeVO;
import com.example.university.dto.StudentGradeVO;
import com.example.university.entity.StudentCourse;
import com.example.university.mapper.StudentCourseMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StudentCourseService extends ServiceImpl<StudentCourseMapper, StudentCourse> {

    public List<GradeVO> getGradesByStudentId(Integer studentId) {
        return baseMapper.findGradesByStudentId(studentId);
    }

    public List<StudentGradeVO> getStudentsWithGradesByCourseId(Integer courseId) {
        return baseMapper.findStudentsByCourseId(courseId);
    }

    public boolean updateGrade(Integer studentId, Integer courseId, BigDecimal score) {
        QueryWrapper<StudentCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId).eq("course_id", courseId);
        
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setScore(score);

        return this.update(studentCourse, wrapper);
    }
} 