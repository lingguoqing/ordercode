package com.swaggerdemo.librarymanagement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swaggerdemo.librarymanagement.common.BaseResponse;
import com.swaggerdemo.librarymanagement.common.PageListResponse;
import com.swaggerdemo.librarymanagement.common.ResultResponse;
import com.swaggerdemo.librarymanagement.domain.Books;
import com.swaggerdemo.librarymanagement.domain.BooksVO;
import com.swaggerdemo.librarymanagement.domain.dto.BooksDTO;
import com.swaggerdemo.librarymanagement.mapper.BooksMapper;
import com.swaggerdemo.librarymanagement.service.BooksService;
import com.swaggerdemo.librarymanagement.utils.PageListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ling
 * @description 针对表【books(图书信息表)】的数据库操作Service实现
 * @createDate 2025-06-15 17:17:21
 */
@Slf4j
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements BooksService {

    @Override
    public BaseResponse<PageListResponse<BooksVO>> pageList(BooksDTO booksDTO) {
        Long bookId = booksDTO.getBookId();
        int pageNum = booksDTO.getPageNum();
        int pageSize = booksDTO.getPageSize();
        Page<Books> booksPage = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Books> booksLambdaQueryWrapper = new LambdaQueryWrapper<>();
        booksLambdaQueryWrapper.eq(bookId != null, Books::getBookId, bookId);
        Page<Books> page = this.page(booksPage, booksLambdaQueryWrapper);
        List<BooksVO> booksVOList = page.getRecords().stream().map(item -> {
            BooksVO booksVO = new BooksVO();
            BeanUtil.copyProperties(item, booksVO);
            booksVO.setBookId(String.valueOf(item.getBookId()));
            return booksVO;
        }).collect(Collectors.toList());
        PageListResponse<BooksVO> pageList = PageListUtil.getPageList(page, booksVOList);
        return ResultResponse.success(200, "OK", pageList);
    }

    @Override
    public BaseResponse<Books> getBookById(Long bookId) {
        LambdaQueryWrapper<Books> booksLambdaQueryWrapper = new LambdaQueryWrapper<>();
        booksLambdaQueryWrapper.eq(Books::getBookId, bookId);
        Books book = this.getOne(booksLambdaQueryWrapper);
        if (book != null) {
            return ResultResponse.success(200, "OK", book);
        } else {
            return ResultResponse.error(404, "该书籍不存在");
        }
    }

    @Override
    public BaseResponse<String> addBook(Books books) {
        boolean saved = this.save(books);
        if (saved) {
            return ResultResponse.success(200, "添加书籍成功");
        } else {
            return ResultResponse.error(500, "添加书籍失败");
        }
    }

    @Override
    public BaseResponse<String> updateBook(Books books) {
        boolean updated = this.updateById(books);
        if (updated) {
            return ResultResponse.success(200, "更新书籍成功");
        } else {
            return ResultResponse.error(500, "更新书籍失败");
        }
    }

    @Override
    public BaseResponse<String> deleteBook(Long bookId) {
        boolean removed = this.removeById(bookId);
        if (removed) {
            return ResultResponse.success(200, "删除成功");
        } else {
            return ResultResponse.error(500, "删除失败");
        }
    }

    @Override
    public BaseResponse<String> deleteBatchBooks(List<Long> bookIds) {
        boolean removed = this.removeByIds(bookIds);
        if (removed) {
            return ResultResponse.success(200, "批量删除书籍成功");
        } else {
            return ResultResponse.error(500, "批量删除书籍失败");
        }
    }
}




