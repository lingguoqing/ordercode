package com.example.university.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher")
public class Teacher {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String gender;
    private String title;
    private String phone;
    private String password;
} 