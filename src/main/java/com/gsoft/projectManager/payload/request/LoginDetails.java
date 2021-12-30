package com.gsoft.projectManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDetails {
    private String usernameOrEmail;
    private String password;
}
