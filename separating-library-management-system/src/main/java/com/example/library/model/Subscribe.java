package com.example.library.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Subscribe {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime subscribeTime;
} 