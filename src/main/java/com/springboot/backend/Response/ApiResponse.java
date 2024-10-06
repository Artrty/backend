package com.springboot.backend.Response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 공통 api 응답 클래스
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status; //응답의 상태 - success, fail, error
    private T data; // 데이터 목록 - success일 경우 실제 전송될 데이터 / fail일 경우 
    private String message; // error일 경우 예외 메세지 응답

    // 응답 성공 시 데이터, 성공 메세지를 포함한 응답 생성
    public static <T> ApiResponse<T> successResponse(T data, String message ) {
        return new ApiResponse<>(SUCCESS_STATUS, data, message);
    }

    // 응답이 성공했으나 반환할 데이터가 없는 경우
    public static ApiResponse<?> successWithNoContent() {
        return new ApiResponse<>(SUCCESS_STATUS, null, null);
    }

    // 요청 데이터의 유효성 검증 실패 시
    public static ApiResponse<?> failResponse(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }
        return new ApiResponse<>(FAIL_STATUS, errors, null);
    }

    // 응답에 오류가 발생했을 경우
    public static ApiResponse<?> errorResponse(String message) {
        return new ApiResponse<>(ERROR_STATUS, null, message);
    }

    private ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
