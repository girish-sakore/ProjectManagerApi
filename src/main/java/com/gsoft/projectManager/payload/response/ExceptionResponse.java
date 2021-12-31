package com.gsoft.projectManager.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class ExceptionResponse implements Serializable {

    private Boolean success;

    private String message;

    private HttpStatus status;

    public ExceptionResponse() {

    }
    public ExceptionResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public ExceptionResponse(Boolean success, String message, HttpStatus httpStatus) {
        this.success = success;
        this.message = message;
        this.status = httpStatus;
    }
}
