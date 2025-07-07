package com.example.university.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("course")
public class Course {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private BigDecimal credit;
    private String type;
    private String classTime;
    private String location;
} 