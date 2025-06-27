package com.example.library.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Book {
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Integer stock;
    private Integer marker;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 