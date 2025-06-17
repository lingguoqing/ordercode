package com.car.onlinecarselectionsystem.config;

import com.car.onlinecarselectionsystem.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "请求体格式错误或参数类型不匹配";
        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            errorMessage += ": " + rootCause.getMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    // 可以添加其他全局异常处理方法
} 