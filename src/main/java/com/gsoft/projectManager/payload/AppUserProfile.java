package com.gsoft.projectManager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUserProfile {
    String username;
    String email;
    String firstName;
    String lastName;
    String number;
    String role;

}
