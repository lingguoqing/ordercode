package com.example.library.dao;

import com.example.library.model.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubscribeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Subscribe subscribe) {
        String sql = "INSERT INTO subscribe (user_id, book_id, subscribe_time) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, subscribe.getUserId(), subscribe.getBookId(), subscribe.getSubscribeTime());
    }

    public List<Subscribe> findByUserId(Long userId) {
        String sql = "SELECT * FROM subscribe WHERE user_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Subscribe.class), userId);
    }
    
    public Subscribe findByUserIdAndBookId(Long userId, Long bookId) {
        String sql = "SELECT * FROM subscribe WHERE user_id = ? AND book_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Subscribe.class), userId, bookId);
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(Long userId, Long bookId) {
        String sql = "DELETE FROM subscribe WHERE user_id = ? AND book_id = ?";
        jdbcTemplate.update(sql, userId, bookId);
    }

    public int countByBookId(Long bookId) {
        String sql = "SELECT COUNT(*) FROM subscribe WHERE book_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, bookId);
    }

    public void deleteByBookId(Long bookId) {
        String sql = "DELETE FROM subscribe WHERE book_id = ?";
        jdbcTemplate.update(sql, bookId);
    }
} 