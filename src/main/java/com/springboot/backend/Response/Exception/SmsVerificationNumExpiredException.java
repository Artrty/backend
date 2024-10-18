package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class SmsVerificationNumExpiredException extends ApiExceptionHandler{

    public SmsVerificationNumExpiredException() {
        super(ErrorCode.SmsVerificationNumExpiredException);
    }


}
