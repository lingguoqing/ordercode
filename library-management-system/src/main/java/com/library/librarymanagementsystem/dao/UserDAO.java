package com.library.librarymanagementsystem.dao;

import com.library.librarymanagementsystem.entity.User;
import com.library.librarymanagementsystem.util.DBUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;

public class UserDAO {
    
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public User findByUsername(String username, String rawPassword) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                String hashedPassword = rs.getString("password");
                
                // 验证密码
                if (passwordEncoder.matches(rawPassword, hashedPassword)) {
                    user.setPassword(hashedPassword); // 存储哈希后的密码，但通常前端不需要明文密码
                    return user;
                }
            }
        }
        return null;
    }
    
    public void register(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 对密码进行哈希处理
            String hashedPassword = passwordEncoder.encode(user.getPassword());

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            
            pstmt.executeUpdate();
        }
    }

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
} 