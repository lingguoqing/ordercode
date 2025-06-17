package com.swaggerdemo.librarymanagement.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PageListResponse<T> implements Serializable {

    
    private static final long serialVersionUID = 7357780382090948794L;

    private long pageNum;
    private long pageSize;
    private long total;
    private List<T> list;
}
