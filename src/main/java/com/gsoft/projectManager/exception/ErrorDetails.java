package com.gsoft.projectManager.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorDetails {
    private String message;
    private String statusCode;
    private LocalDateTime timestamp;
    private String URI;

    public ErrorDetails(String message, String statusCode, LocalDateTime timestamp, String URI) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.URI = URI;
    }

}
