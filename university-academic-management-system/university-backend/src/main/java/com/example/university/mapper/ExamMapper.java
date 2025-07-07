package com.example.university.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.university.dto.ExamAdminVO;
import com.example.university.dto.ExamVO;
import com.example.university.entity.Exam;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExamMapper extends BaseMapper<Exam> {

    @Select("SELECT c.name as courseName, e.exam_time as examTime, e.location " +
            "FROM exam e " +
            "JOIN course c ON e.course_id = c.id " +
            "JOIN student_course sc ON c.id = sc.course_id " +
            "WHERE sc.student_id = #{studentId}")
    List<ExamVO> findExamsByStudentId(Integer studentId);

    @Select("SELECT e.id, e.course_id as courseId, c.name as courseName, e.exam_time as examTime, e.location " +
            "FROM exam e " +
            "JOIN course c ON e.course_id = c.id")
    List<ExamAdminVO> findAllExamsWithCourseName();

} 