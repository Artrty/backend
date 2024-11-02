package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class FileCreationFailedException extends ApiExceptionHandler{
    public FileCreationFailedException() {
        super(ErrorCode.FileCreationFailedException);
    }
}
