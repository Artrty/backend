package com.springboot.backend.Response.Exception;

import com.springboot.backend.Response.ErrorCode;

public class EventBoardLoadException extends ApiExceptionHandler{
    public EventBoardLoadException() {
        super(ErrorCode.EventBoardLoadException);
    }
}
