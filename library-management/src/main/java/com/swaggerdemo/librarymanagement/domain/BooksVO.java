package com.swaggerdemo.librarymanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BooksVO implements Serializable {
    private static final long serialVersionUID = 760647612169550616L;

    private String bookId;

    private String isbn;

    private String title;

    private String subtitle;

    private String originalTitle;

    private String author;

    private String authorBio;

    private String translator;

    private String publisher;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishDate;

    private String publishEdition;

    private String publishPlace;

    private Integer categoryId;

    private String categoryName;

    private String tags;

    private String language;

    private String format;

    private Integer pageCount;

    private Integer wordCount;

    private String size;

    private BigDecimal weight;

    private String summary;

    private String catalog;

    private String preface;

    private String introduction;

    private BigDecimal price;

    private BigDecimal discountPrice;

    private BigDecimal marketPrice;

    private Integer stockQuantity;

    private Integer salesVolume;

    private BigDecimal rating;

    private Integer ratingCount;

    private Integer hasEbook;

    private BigDecimal ebookPrice;

    private String ebookFormat;

    private String coverImage;

    private String sampleChapter;

    private String seriesName;

    private String seriesNumber;

    private String awards;

    private String recommendation;

    private Integer status;

    private Integer isRecommended;

    private Integer isBestseller;

    private Integer isNew;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastRestockTime;
}