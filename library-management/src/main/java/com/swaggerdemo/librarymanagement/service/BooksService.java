package com.swaggerdemo.librarymanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swaggerdemo.librarymanagement.common.BaseResponse;
import com.swaggerdemo.librarymanagement.common.PageListResponse;
import com.swaggerdemo.librarymanagement.domain.Books;
import com.swaggerdemo.librarymanagement.domain.BooksVO;
import com.swaggerdemo.librarymanagement.domain.dto.BooksDTO;

import java.util.List;

/**
 * @author Ling
 * @description 针对表【books(图书信息表)】的数据库操作Service
 * @createDate 2025-06-15 17:17:21
 */
public interface BooksService extends IService<Books> {

    BaseResponse<PageListResponse<BooksVO>> pageList(BooksDTO booksDTO);

    BaseResponse<Books> getBookById(Long bookId);

    BaseResponse<String> addBook(Books books);

    BaseResponse<String> updateBook(Books books);

    BaseResponse<String> deleteBook(Long bookId);

    BaseResponse<String> deleteBatchBooks(List<Long> bookIds);
}
