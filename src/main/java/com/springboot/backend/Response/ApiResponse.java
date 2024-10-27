package com.springboot.backend.Response;

import lombok.Getter;

// 공통 api 응답 클래스
@Getter
public class ApiResponse<T> {

    private T data;          // 실제 데이터
    private String message;  // 메시지
    private String code;     // 사용자 정의 코드

    // 성공 응답
    public static ApiResponse<?> successResponse(SuccessCode successCode, Object data) {
        return new ApiResponse<>( data, successCode.getMessage(), successCode.getCode());
    }

    // 실패 응답
    public static ApiResponse<?> errorResponse(ErrorCode errorCode) {
        return new ApiResponse<>(null, errorCode.getMessage(), errorCode.getCode());
    }

    private ApiResponse(T data, String message, String code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }
}


