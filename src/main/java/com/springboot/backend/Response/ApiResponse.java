package com.springboot.backend.Response;

import lombok.Getter;

// 공통 api 응답 클래스
@Getter
public class ApiResponse<T> {

    private String status;   // success or error
    private int httpStatus;  // HTTP 상태 코드
    private T data;          // 실제 데이터
    private String message;  // 메시지
    private String code;     // 사용자 정의 코드

    // 성공 응답
    public static ApiResponse<?> successResponse(SuccessCode successCode, Object data) {
        return new ApiResponse<>("success", data, successCode.getMessage(), successCode.getCode(), successCode.getStatus());
    }

    // 실패 응답
    public static ApiResponse<?> errorResponse(ErrorCode errorCode) {
        return new ApiResponse<>("error", null, errorCode.getMessage(), errorCode.getCode(), errorCode.getStatus());
    }

    private ApiResponse(String status, T data, String message, String code, int httpStatus) {
        this.status = status;
        this.httpStatus = httpStatus;
        this.data = data;
        this.message = message;
        this.code = code;
    }
}


