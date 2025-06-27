package com.example.library.service;

import com.example.library.dao.UserDao;
import com.example.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean register(User user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            return false; // 用户名已存在
        }
        // TODO: 在实际应用中，密码应该被加密存储
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("user"); // 默认角色为普通用户
        user.setCreateTime(LocalDateTime.now());
        userDao.save(user);
        return true;
    }

    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            // TODO: 密码应该使用加密匹配
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userDao.update(user);
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAllUsers() {
        return userDao.findAll();
    }
} 