package com.example.university.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.university.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    @Select("SELECT * FROM course WHERE id NOT IN (SELECT course_id FROM student_course WHERE student_id = #{studentId})")
    List<Course> findAvailableCoursesForStudent(@Param("studentId") Integer studentId);

    @Select("SELECT c.* FROM course c JOIN teacher_course tc ON c.id = tc.course_id WHERE tc.teacher_id = #{teacherId}")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Integer teacherId);
} 