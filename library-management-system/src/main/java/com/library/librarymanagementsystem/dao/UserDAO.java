package com.library.librarymanagementsystem.dao;

import com.library.librarymanagementsystem.entity.User;
import com.library.librarymanagementsystem.util.DBUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.text.SimpleDateFormat;

public class UserDAO {
    
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
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
                    user.setPassword(hashedPassword);
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setRealName(rs.getString("real_name"));
                    user.setGender(rs.getString("gender"));
                    user.setBirthDate(rs.getDate("birth_date"));
                    user.setAddress(rs.getString("address"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return user;
                }
            }
        }
        return null;
    }
    
    public void register(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, phone, real_name, gender, birth_date, address) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 对密码进行哈希处理
            String hashedPassword = passwordEncoder.encode(user.getPassword());

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getRealName());
            pstmt.setString(6, user.getGender());
            pstmt.setDate(7, user.getBirthDate() != null ? new java.sql.Date(user.getBirthDate().getTime()) : null);
            pstmt.setString(8, user.getAddress());
            
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

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, phone = ?, " +
                    "real_name = ?, gender = ?, birth_date = ?, address = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // 对密码进行哈希处理
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getRealName());
            pstmt.setString(6, user.getGender());
            pstmt.setDate(7, user.getBirthDate() != null ? new java.sql.Date(user.getBirthDate().getTime()) : null);
            pstmt.setString(8, user.getAddress());
            pstmt.setInt(9, user.getId());
            
            pstmt.executeUpdate();
        }
    }
} 