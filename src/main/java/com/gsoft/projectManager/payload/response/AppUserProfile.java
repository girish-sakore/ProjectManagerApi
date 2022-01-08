package com.gsoft.projectManager.payload.response;

import com.gsoft.projectManager.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AppUserProfile {
    String username;
    String email;
    String firstName;
    String lastName;
    String number;
    List<Role> roles;

}
