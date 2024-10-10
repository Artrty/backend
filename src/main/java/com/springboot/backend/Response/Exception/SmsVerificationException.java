package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class SmsVerificationException extends ApiExceptionHandler{

    public SmsVerificationException() {
        super(ErrorCode.SmsVerificationException);
    }

}
