package com.gsoft.projectManager.exception;

import com.gsoft.projectManager.payload.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> catchException(ResponseStatusException exception) {
        String message = exception.getMessage();
        HttpStatus status = exception.getStatus();

        ExceptionResponse exceptionResponse = new ExceptionResponse();
//        exceptionResponse.setSuccess(Boolean.FALSE);
        exceptionResponse.setMessage(message);

        return new ResponseEntity< >(exceptionResponse, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(ResourceNotFoundException exception) {
        System.out.println("Exception===> " + exception.getMessage());
        ExceptionResponse exceptionResponse = exception.getExceptionResponse();
        System.out.println("----> " + exceptionResponse);
        return new ResponseEntity< >(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(BadRequestException exception) {
        System.out.println("exception " + exception.getExceptionResponse());
        System.out.println("exception " + exception.getMessage());
        System.out.println("exception " + exception.getCause());
        System.out.println("exception " + Arrays.toString(exception.getStackTrace()));
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
//        System.out.println("exceptionResponse " + exceptionResponse.toString());

        return new ResponseEntity< >(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
