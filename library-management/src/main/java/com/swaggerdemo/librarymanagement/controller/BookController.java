package com.swaggerdemo.librarymanagement.controller;

import com.swaggerdemo.librarymanagement.common.BaseResponse;
import com.swaggerdemo.librarymanagement.common.PageListResponse;
import com.swaggerdemo.librarymanagement.domain.Books;
import com.swaggerdemo.librarymanagement.domain.BooksVO;
import com.swaggerdemo.librarymanagement.domain.dto.BooksDTO;
import com.swaggerdemo.librarymanagement.service.BooksService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Resource
    private BooksService booksService;

    @PostMapping("/page")
    public BaseResponse<PageListResponse<BooksVO>> pageList(@RequestBody BooksDTO booksDTO) {
        return booksService.pageList(booksDTO);
    }

    @GetMapping("/{bookId}")
    public BaseResponse<Books> getBookById(@PathVariable Long bookId) {
        return booksService.getBookById(bookId);
    }

    @PostMapping("/add")
    public BaseResponse<String> addBook(@RequestBody Books books) {
        return booksService.addBook(books);
    }

    @PutMapping("/update")
    public BaseResponse<String> updateBook(@RequestBody Books books) {
        return booksService.updateBook(books);
    }

    @DeleteMapping("/{bookId}")
    public BaseResponse<String> deleteBook(@PathVariable Long bookId) {
        return booksService.deleteBook(bookId);
    }

    @DeleteMapping("/batchDelete")
    public BaseResponse<String> deleteBatchBooks(@RequestBody List<Long> bookIds) {
        return booksService.deleteBatchBooks(bookIds);
    }
}
