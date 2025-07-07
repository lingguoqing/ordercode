package com.example.library.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Subscribe implements Serializable {
    @Serial
    private static final long serialVersionUID = 685147669621336171L;
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime subscribeTime;
} 