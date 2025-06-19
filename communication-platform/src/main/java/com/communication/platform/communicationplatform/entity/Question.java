package com.communication.platform.communicationplatform.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Question {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private LocalDateTime createTime;
    private Integer viewCount;
    
    // 非数据库字段，用于显示
    private String username;
} 