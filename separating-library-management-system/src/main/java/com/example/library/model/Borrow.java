package com.example.library.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Borrow {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private String status; // "borrowed" or "returned"
} 