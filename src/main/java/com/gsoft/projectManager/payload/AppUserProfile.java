package com.gsoft.projectManager.payload;

import com.gsoft.projectManager.appuser.Role;
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
