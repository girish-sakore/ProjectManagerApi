package com.gsoft.projectManager.payload.response;

import lombok.Data;

@Data
public class ConfirmationResponse {
    private final Boolean success;
    private final String message;
}
