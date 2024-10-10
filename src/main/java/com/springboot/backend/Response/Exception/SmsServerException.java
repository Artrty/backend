package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class SmsServerException extends ApiExceptionHandler{

    public SmsServerException() {
        super(ErrorCode.SmsServerException);
    }

}
