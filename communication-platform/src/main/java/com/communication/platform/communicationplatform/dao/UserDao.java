package com.communication.platform.communicationplatform.dao;

import com.communication.platform.communicationplatform.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserDao {
    // 插入新用户
    int insert(User user);
    
    // 更新用户信息
    int updateUser(User user);
    
    // 更新最后登录时间
    int updateLastLoginTime(Long id);
    
    // 根据用户名查找用户
    User findByUsername(String username);
    
    // 根据邮箱查找用户
    User findByEmail(String email);
    
    // 根据ID查找用户
    User findById(Long id);
    
    // 用户登录（只允许状态正常的用户登录）
    User login(@Param("username") String username, @Param("password") String password);
} 