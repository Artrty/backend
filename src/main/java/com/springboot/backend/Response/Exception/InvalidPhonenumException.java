package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class InvalidPhonenumException extends ApiExceptionHandler{

    public InvalidPhonenumException() {
        super(ErrorCode.InvalidPhonenumException);
    }
}
