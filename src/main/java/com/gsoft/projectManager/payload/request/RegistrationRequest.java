package com.gsoft.projectManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class RegistrationRequest {
    private final String email;
    private final String username;
    private final String password;
    private final String number;
    private final String firstName;
    private final String lastName;
}
