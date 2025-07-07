package com.example.university.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeVO {
    private Integer courseId;
    private String courseName;
    private BigDecimal credit;
    private String courseType;
    private BigDecimal score;
} 