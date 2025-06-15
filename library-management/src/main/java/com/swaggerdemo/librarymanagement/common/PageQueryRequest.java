package com.swaggerdemo.librarymanagement.common;

import lombok.Data;

import java.io.Serializable;


@Data
public class PageQueryRequest implements Serializable {

    
    private static final long serialVersionUID = 207848359054670844L;

    private int pageNum = 1;
    private int pageSize = 10;

}
