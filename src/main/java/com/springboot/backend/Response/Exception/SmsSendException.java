package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class SmsSendException extends ApiExceptionHandler {

    public SmsSendException() {
        super(ErrorCode.SmsSendException);
    }

}
