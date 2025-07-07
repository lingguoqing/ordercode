package com.example.library.dao;

import com.example.library.model.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BorrowDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Borrow borrow) {
        String sql = "INSERT INTO borrow (user_id, book_id, borrow_time, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, borrow.getUserId(), borrow.getBookId(), borrow.getBorrowTime(), borrow.getStatus());
    }

    public void update(Borrow borrow) {
        String sql = "UPDATE borrow SET return_time = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, borrow.getReturnTime(), borrow.getStatus(), borrow.getId());
    }

    public Borrow findByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status) {
        String sql = "SELECT * FROM borrow WHERE user_id = ? AND book_id = ? AND status = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Borrow.class), userId, bookId, status);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Borrow> findByUserId(Long userId) {
        String sql = "SELECT * FROM borrow WHERE user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Borrow.class), userId);
    }

    public List<Borrow> findAll() {
        String sql = "SELECT * FROM borrow";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Borrow.class));
    }

    public int countByBookId(Long bookId) {
        String sql = "SELECT COUNT(*) FROM borrow WHERE book_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, bookId);
    }

    public void deleteByBookId(Long bookId) {
        String sql = "DELETE FROM borrow WHERE book_id = ?";
        jdbcTemplate.update(sql, bookId);
    }
} 