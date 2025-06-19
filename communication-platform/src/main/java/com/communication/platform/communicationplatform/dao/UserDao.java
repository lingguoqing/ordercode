package com.communication.platform.communicationplatform.dao;

import com.communication.platform.communicationplatform.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserDao {
    // 插入新用户
    int insert(User user);
    
    // 根据用户名查找用户
    User findByUsername(String username);
    
    // 根据ID查找用户
    User findById(Long id);
    
    // 用户登录
    User login(@Param("username") String username, @Param("password") String password);
} 