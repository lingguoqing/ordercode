package com.example.library.service;

import com.example.library.dao.BorrowDao;
import com.example.library.model.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private BorrowDao borrowDao;

    @Autowired
    private BookService bookService;

    @Transactional
    public boolean borrowBook(Long userId, Long bookId) {
        // 新增校验：同一用户同一本书有未归还记录时，禁止再次借阅
        Borrow exist = borrowDao.findByUserIdAndBookIdAndStatus(userId, bookId, "borrowed");
        if (exist != null) {
            return false;
        }
        // 1. 检查库存并减少库存
        boolean success = bookService.borrowBook(bookId);
        if (success) {
            // 2. 创建借阅记录
            Borrow borrow = new Borrow();
            borrow.setUserId(userId);
            borrow.setBookId(bookId);
            borrow.setBorrowTime(LocalDateTime.now());
            borrow.setStatus("borrowed");
            borrowDao.save(borrow);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean returnBook(Long userId, Long bookId) {
        // 1. 查找未还的借阅记录
        Borrow borrow = borrowDao.findByUserIdAndBookIdAndStatus(userId, bookId, "borrowed");
        if (borrow != null) {
            // 2. 更新借阅记录状态
            borrow.setReturnTime(LocalDateTime.now());
            borrow.setStatus("returned");
            borrowDao.update(borrow);
            
            // 3. 增加图书库存
            bookService.returnBook(bookId);
            return true;
        }
        return false; // 没有找到对应的借阅记录
    }

    public List<Borrow> findBorrowsByUserId(Long userId) {
        return borrowDao.findByUserId(userId);
    }
    
    public List<Borrow> findAllBorrows() {
        return borrowDao.findAll();
    }
} 