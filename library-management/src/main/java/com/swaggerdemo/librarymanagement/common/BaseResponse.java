package com.swaggerdemo.librarymanagement.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    /**
     * @param code    状态码
     * @param message 信息
     * @param data    数据
     */
    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @param code    状态码
     * @param message 信息
     */
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
