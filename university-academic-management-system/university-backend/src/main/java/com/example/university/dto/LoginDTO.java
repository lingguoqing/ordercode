package com.example.university.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
    /**
     * 角色: admin, student, teacher
     */
    private String role;
} 