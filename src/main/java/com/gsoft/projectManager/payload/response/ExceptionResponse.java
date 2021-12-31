package com.gsoft.projectManager.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ExceptionResponse implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 123L;

    @JsonProperty("message")
    private String message;

    @JsonProperty("time-stamp")
    private LocalDateTime timeStamp;

    @JsonProperty("error")
    private HttpStatus status;

    public ExceptionResponse() {}

    public ExceptionResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus;
    }
}
