package com.gsoft.projectManager.exception;

import com.gsoft.projectManager.payload.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> catchException(ResponseStatusException exception) {
        String message = exception.getMessage();
        HttpStatus status = exception.getStatus();

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setSuccess(Boolean.FALSE);
        exceptionResponse.setMessage(message);

        return new ResponseEntity< >(exceptionResponse, status);
    }


}
