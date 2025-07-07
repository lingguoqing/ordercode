package com.example.university.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("student")
public class Student {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private Date enrollmentDate;
    private Integer classId;
    private String password;
} 