package com.car.onlinecarselectionsystem.service;

import com.car.onlinecarselectionsystem.dto.UserLoginRequest;
import com.car.onlinecarselectionsystem.entity.User;

public interface UserService {
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功返回true，否则返回false
     */
    boolean register(User user);

    /**
     * 用户登录
     * @param loginRequest 用户登录请求（包含用户名和密码）
     * @return 登录成功返回JWT Token，否则返回null
     */
    String login(UserLoginRequest loginRequest);

    /**
     * 通过用户ID获取用户信息
     */
    User getUserById(Integer userId);
} 