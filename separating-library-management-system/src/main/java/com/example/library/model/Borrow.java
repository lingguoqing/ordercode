package com.example.library.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Borrow implements Serializable {
    @Serial
    private static final long serialVersionUID = -3027494333758989569L;
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private String status; // "borrowed" or "returned"
} 