package com.example.library.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = -6020726785678730902L;
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