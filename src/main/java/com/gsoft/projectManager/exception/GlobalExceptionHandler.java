package com.gsoft.projectManager.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<?> myException(ResponseStatusException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                exception.getMessage(),
                exception.getStatus().toString(),
                LocalDateTime.now(),
                request.getDescription(false).substring(4)
                        );
        LOGGER.error("Handled Exception: " +  exception);

        return new ResponseEntity<>(errorDetails, exception.getStatus());
    }
}
