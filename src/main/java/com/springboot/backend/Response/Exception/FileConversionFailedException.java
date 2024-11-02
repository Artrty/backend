package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class FileConversionFailedException extends ApiExceptionHandler{
    public FileConversionFailedException() {
        super(ErrorCode.FileConversionFailedException);
    }
}
