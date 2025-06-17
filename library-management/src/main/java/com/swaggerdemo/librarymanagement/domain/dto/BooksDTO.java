package com.swaggerdemo.librarymanagement.domain.dto;

import com.swaggerdemo.librarymanagement.common.PageQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class BooksDTO extends PageQueryRequest implements Serializable {
    private static final long serialVersionUID = 760647612169550616L;

    private Long bookId;

}