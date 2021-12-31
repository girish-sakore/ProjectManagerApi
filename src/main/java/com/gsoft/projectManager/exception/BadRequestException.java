package com.gsoft.projectManager.exception;

import com.gsoft.projectManager.payload.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private ExceptionResponse exceptionResponse;

	public BadRequestException(ExceptionResponse exceptionResponse) {
		super();
		this.exceptionResponse = exceptionResponse;
	}

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionResponse getExceptionResponse() {
		return exceptionResponse;
	}
}
