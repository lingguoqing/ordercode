package com.example.university.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExamAdminVO {
    private Integer id;
    private Integer courseId;
    private String courseName;
    private LocalDateTime examTime;
    private String location;
}