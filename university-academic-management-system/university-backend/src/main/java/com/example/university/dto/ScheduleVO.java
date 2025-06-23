package com.example.university.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ScheduleVO {
    private String courseName;
    private BigDecimal credit;
    private String classTime;
    private String location;
    private String teacherName;
} 