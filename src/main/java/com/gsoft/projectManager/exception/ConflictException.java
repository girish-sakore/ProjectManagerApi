package com.gsoft.projectManager.exception;

import com.gsoft.projectManager.payload.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private ExceptionResponse exceptionResponse;

    private String message;

    public ConflictException(){}

    public ConflictException(ExceptionResponse exceptionResponse) {
        super();
        this.exceptionResponse = exceptionResponse;
    }

    public ConflictException(String message) {
        super(message);
        this.message = message;
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionResponse getExceptionResponse() {
        return exceptionResponse;
    }

    public void setExceptionResponse(ExceptionResponse exceptionResponse) {
        this.exceptionResponse = exceptionResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
