package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class UserExistsException extends ApiExceptionHandler{
    public UserExistsException() {
        super(ErrorCode.UserExistsException);
    }

}
