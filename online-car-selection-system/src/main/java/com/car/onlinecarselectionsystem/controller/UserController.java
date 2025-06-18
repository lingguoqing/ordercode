package com.car.onlinecarselectionsystem.controller;

import com.car.onlinecarselectionsystem.dto.UserLoginRequest;
import com.car.onlinecarselectionsystem.dto.UserRegisterRequest;
import com.car.onlinecarselectionsystem.entity.User;
import com.car.onlinecarselectionsystem.response.ApiResponse;
import com.car.onlinecarselectionsystem.service.UserService;
import com.car.onlinecarselectionsystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody UserRegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());

        if (userService.register(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("注册成功"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("用户名已存在"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserLoginRequest loginRequest) {
        String jwt = userService.login(loginRequest);
        if (jwt != null) {
            return ResponseEntity.ok(ApiResponse.success("登录成功", jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("用户名或密码错误"));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("未提供有效的Token！"));
        }
        String token = authHeader.substring(7);
        Integer userId;
        try {
            userId = jwtUtil.extractClaim(token, claims -> (Integer) claims.get("userId"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Token无效或已过期！"));
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("用户不存在！"));
        }
        // 不返回密码等敏感信息
        user.setPassword(null);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
} 