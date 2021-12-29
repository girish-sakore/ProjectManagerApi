package com.gsoft.projectManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordRequest {
    private final String oldPassword;
    private final String newPassword;
}
