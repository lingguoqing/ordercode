package com.communication.platform.communicationplatform.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Answer {
    private Long id;
    private Long questionId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;
    
    // 非数据库字段，用于显示
    private String username;
} 