package com.example.library.service;

import com.example.library.dao.BookDao;
import com.example.library.dao.BorrowDao;
import com.example.library.dao.SubscribeDao;
import com.example.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private BorrowDao borrowDao;
    @Autowired
    private SubscribeDao subscribeDao;

    public List<Book> findAllBooks() {
        return bookDao.findAll();
    }

    public Book findBookById(Long id) {
        return bookDao.findById(id);
    }

    public void addBook(Book book) {
        book.setCreateTime(LocalDateTime.now());
        book.setMarker(book.getStock());
        bookDao.save(book);
    }

    public void updateBook(Book book) {
        book.setUpdateTime(LocalDateTime.now());
        bookDao.update(book);
    }

    public boolean deleteBook(Long id) {
        Book book = bookDao.findById(id);
        if (book == null) return false;
        if (book.getStock() == null || book.getMarker() == null || !book.getStock().equals(book.getMarker())) {
            return false;
        }
        bookDao.deleteById(id);
        return true;
    }

    @Transactional
    public boolean borrowBook(Long bookId) {
        Book book = bookDao.findById(bookId);
        if (book != null && book.getStock() > 0) {
            bookDao.updateStock(bookId, -1);
            return true;
        }
        return false;
    }

    @Transactional
    public void returnBook(Long bookId) {
        bookDao.updateStock(bookId, 1);
    }
} 