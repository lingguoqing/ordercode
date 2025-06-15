package com.swaggerdemo.librarymanagement.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swaggerdemo.librarymanagement.common.PageListResponse;
import org.springframework.lang.NonNull;

import java.util.List;

public class PageListUtil {


    @NonNull
    public static <T> PageListResponse<T> getPageList(Page<?> page, List<T> pageList) {
        PageListResponse<T> pageListResponse = new PageListResponse<>();
        pageListResponse.setPageNum(page.getCurrent());
        pageListResponse.setPageSize(page.getSize());
        pageListResponse.setTotal(page.getTotal());
        pageListResponse.setList(pageList);
        return pageListResponse;
    }

}
