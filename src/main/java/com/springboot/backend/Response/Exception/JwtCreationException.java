package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class JwtCreationException extends ApiExceptionHandler{
    public JwtCreationException() {
        super(ErrorCode.JwtCreationException);
    }
}
