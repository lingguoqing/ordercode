package com.example.university.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StudentGradeVO {
    private Integer studentId;
    private String studentName;
    private BigDecimal score;
} 