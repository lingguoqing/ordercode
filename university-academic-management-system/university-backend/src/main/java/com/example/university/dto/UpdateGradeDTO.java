package com.example.university.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateGradeDTO {
    private Integer studentId;
    private Integer courseId;
    private BigDecimal score;
} 