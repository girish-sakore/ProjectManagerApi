package com.gsoft.projectManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InternalServerException(String message) {
        super(message);
    }

}