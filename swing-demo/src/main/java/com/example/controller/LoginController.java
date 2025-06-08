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

public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("用户名和密码不能为空");
            return;
        }
        
        try (SqlSession sqlSession = DatabaseUtil.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                    .eq("username", username)
                    .eq("password", password)
            );
            
            if (user != null) {
                messageLabel.setText("登录成功");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/personality_analysis.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("易通文化 - 八字分析");
                    stage.sizeToScene();
                } catch (Exception e) {
                    e.printStackTrace();
                    messageLabel.setText("加载八字分析页面失败");
                }
            } else {
                messageLabel.setText("用户名或密码错误");
            }
        }
    }
    
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("加载注册页面失败");
        }
    }
} 