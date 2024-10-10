package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class LoginException extends ApiExceptionHandler{

    public LoginException() {
        super(ErrorCode.LoginException);
    }

}
