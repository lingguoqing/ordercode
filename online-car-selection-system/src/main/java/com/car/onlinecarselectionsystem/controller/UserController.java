package com.car.onlinecarselectionsystem.controller;

import com.car.onlinecarselectionsystem.dto.UserLoginRequest;
import com.car.onlinecarselectionsystem.dto.UserRegisterRequest;
import com.car.onlinecarselectionsystem.entity.User;
import com.car.onlinecarselectionsystem.response.ApiResponse;
import com.car.onlinecarselectionsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody UserRegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());

        if (userService.register(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User registered successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error("Username already exists!"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody UserLoginRequest loginRequest) {
        String jwt = userService.login(loginRequest);
        if (jwt != null) {
            return ResponseEntity.ok(ApiResponse.success("Login successful!", jwt));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Invalid username or password!"));
        }
    }
} 