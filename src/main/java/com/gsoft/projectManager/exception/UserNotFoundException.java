package com.gsoft.projectManager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    private static final long SerialVersionUID = 1L;
    private static final String MESSAGE = "User with username %s not found.";

    private final String username;
    private final String message;

    public UserNotFoundException(String username) {
        this.username = username;
        this.message = String.format(MESSAGE, username);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
