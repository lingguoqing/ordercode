package com.example.university.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher_course")
public class TeacherCourse {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer teacherId;
    private Integer courseId;
} 