package com.car.onlinecarselectionsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.car.onlinecarselectionsystem.dto.UserLoginRequest;
import com.car.onlinecarselectionsystem.entity.User;
import com.car.onlinecarselectionsystem.mapper.UserMapper;
import com.car.onlinecarselectionsystem.service.UserService;
import com.car.onlinecarselectionsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return false; // 用户名已存在
        }
        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 插入新用户
        return userMapper.insert(user) > 0;
    }

    @Override
    public String login(UserLoginRequest loginRequest) {
        // 根据用户名查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginRequest.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        // 检查用户是否存在且密码是否匹配
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // 登录成功，生成JWT Token
            return jwtUtil.generateToken(user.getUsername(), user.getUserId());
        }
        return null; // 登录失败
    }
} 