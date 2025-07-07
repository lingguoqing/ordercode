package com.example.university.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("student_course")
public class StudentCourse {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private BigDecimal score;
} 