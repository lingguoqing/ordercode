package com.example.controller;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.util.DatabaseUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegisterController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String email = emailField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            messageLabel.setText("所有字段都必须填写");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("两次输入的密码不一致");
            return;
        }
        
        try (SqlSession sqlSession = DatabaseUtil.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            
            // 检查用户名是否已存在
            User existingUser = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                    .eq("username", username)
            );
            
            if (existingUser != null) {
                messageLabel.setText("用户名已存在");
                return;
            }
            
            // 创建新用户
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            newUser.setCreateTime(currentTime);
            newUser.setUpdateTime(currentTime);
            
            userMapper.insert(newUser);
            sqlSession.commit();
            
            messageLabel.setText("注册成功");
            handleBackToLogin();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("注册失败：" + e.getMessage());
        }
    }
    
    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("返回登录页面失败");
        }
    }
} 