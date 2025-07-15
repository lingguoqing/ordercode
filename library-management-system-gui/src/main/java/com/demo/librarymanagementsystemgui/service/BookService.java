package com.demo.librarymanagementsystemgui.service;

import com.demo.librarymanagementsystemgui.dao.BookDAO;
import com.demo.librarymanagementsystemgui.entity.Book;

import java.util.List;

public class BookService {
    private BookDAO bookDAO = new BookDAO();

    // 添加图书
    public boolean addBook(Book book) {
        return bookDAO.addBook(book);
    }

    // 删除图书
    public boolean deleteBook(int id) {
        return bookDAO.deleteBook(id);
    }

    // 修改图书
    public boolean updateBook(Book book) {
        return bookDAO.updateBook(book);
    }

    // 查询所有图书
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    // 按ID查询图书
    public Book getBookById(int id) {
        return bookDAO.getBookById(id);
    }

    // 模糊搜索并分页
    public List<Book> searchBooks(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return bookDAO.searchBooks(keyword, offset, pageSize);
    }

    // 统计模糊搜索结果总数
    public int countBooks(String keyword) {
        return bookDAO.countBooks(keyword);
    }
} 