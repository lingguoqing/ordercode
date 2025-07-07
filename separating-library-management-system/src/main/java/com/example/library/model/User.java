package com.example.library.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 3408306093110709928L;
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String role; // "user" or "admin"
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 