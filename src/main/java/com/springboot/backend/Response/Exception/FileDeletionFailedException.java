package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class FileDeletionFailedException extends ApiExceptionHandler{
    public FileDeletionFailedException() {
        super(ErrorCode.FileDeletionFailedException);
    }
}

