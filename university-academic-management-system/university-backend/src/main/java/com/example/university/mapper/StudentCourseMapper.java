package com.example.university.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.university.dto.GradeVO;
import com.example.university.dto.StudentGradeVO;
import com.example.university.entity.StudentCourse;
import org.apache.ibatis.annotations.Select;
import com.example.university.dto.ScheduleVO;

import java.util.List;

public interface StudentCourseMapper extends BaseMapper<StudentCourse> {

    @Select("SELECT c.id as courseId, c.name as courseName, c.credit, c.type as courseType, sc.score " +
            "FROM student_course sc " +
            "JOIN course c ON sc.course_id = c.id " +
            "WHERE sc.student_id = #{studentId}")
    List<GradeVO> findGradesByStudentId(Integer studentId);

    @Select("SELECT s.id as studentId, s.name as studentName, sc.score " +
            "FROM student_course sc " +
            "JOIN student s ON sc.student_id = s.id " +
            "WHERE sc.course_id = #{courseId}")
    List<StudentGradeVO> findStudentsByCourseId(Integer courseId);

    @Select("SELECT c.name as courseName, c.credit, c.class_time as classTime, c.location, t.name as teacherName " +
            "FROM student_course sc " +
            "JOIN course c ON sc.course_id = c.id " +
            "LEFT JOIN teacher_course tc ON c.id = tc.course_id " +
            "LEFT JOIN teacher t ON tc.teacher_id = t.id " +
            "WHERE sc.student_id = #{studentId}")
    List<ScheduleVO> findScheduleByStudentId(Integer studentId);
} 