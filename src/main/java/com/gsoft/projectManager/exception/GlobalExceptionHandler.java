package com.gsoft.projectManager.exception;

import com.gsoft.projectManager.payload.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(ResourceNotFoundException exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity< >(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(BadRequestException exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity< >(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(InternalServerException exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        return new ResponseEntity< >(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(UserNotFoundException exception, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity< >(exceptionResponse, HttpStatus.NOT_FOUND);
    }

}
