package com.swaggerdemo.librarymanagement.common;

import lombok.Data;

/**
 * 返回工具类
 */
@Data
public class ResultResponse {


    public static <T> BaseResponse<T> success(int code, String message) {
        return new BaseResponse<>(code, message);
    }

    public static <T> BaseResponse<T> success(int code, String message, T data) {
        return new BaseResponse<>(code, message, data);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message);
    }


}
