package com.example.university.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExamVO {
    private String courseName;
    private LocalDateTime examTime;
    private String location;
} 