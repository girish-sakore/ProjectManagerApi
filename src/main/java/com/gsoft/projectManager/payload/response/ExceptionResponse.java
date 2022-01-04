package com.gsoft.projectManager.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonPropertyOrder({"error", "message", "time-stamp", "source-uri"})
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

    @JsonProperty("source-uri")
    private String uri;

    public ExceptionResponse() {}

    public ExceptionResponse(String message, HttpStatus httpStatus, WebRequest request) {
        this.message = message;
        this.status = httpStatus;
        this.uri = request.getDescription(false).substring(4);
        this.timeStamp = LocalDateTime.now();
    }
}
