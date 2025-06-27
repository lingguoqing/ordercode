package com.example.library.dao;

import com.example.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Book book) {
        String sql = "INSERT INTO book (title, author, publisher, isbn, stock, marker, description, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublisher(), book.getIsbn(), book.getStock(), book.getMarker(), book.getDescription(), book.getCreateTime());
    }

    public void update(Book book) {
        String sql = "UPDATE book SET title = ?, author = ?, publisher = ?, isbn = ?, stock = ?, description = ?, update_time = ? WHERE id = ?";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getPublisher(), book.getIsbn(), book.getStock(), book.getDescription(), book.getUpdateTime(), book.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM book WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Book findById(Long id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Book.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Book> findAll() {
        String sql = "SELECT * FROM book";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class));
    }
    
    public void updateStock(Long bookId, int change) {
        String sql = "UPDATE book SET stock = stock + ? WHERE id = ?";
        jdbcTemplate.update(sql, change, bookId);
    }

    public void updateMarker(Long bookId, int change) {
        String sql = "UPDATE book SET marker = marker + ? WHERE id = ?";
        jdbcTemplate.update(sql, change, bookId);
    }
} 