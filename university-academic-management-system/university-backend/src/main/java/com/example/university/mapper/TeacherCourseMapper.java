package com.example.university.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.university.entity.Course;
import com.example.university.entity.TeacherCourse;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherCourseMapper extends BaseMapper<TeacherCourse> {

    @Select("SELECT c.* FROM course c JOIN teacher_course tc ON c.id = tc.course_id WHERE tc.teacher_id = #{teacherId}")
    List<Course> findCoursesByTeacherId(Integer teacherId);
} 