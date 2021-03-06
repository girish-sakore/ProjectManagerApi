package com.gsoft.projectManager.service;

import com.gsoft.projectManager.model.AppUser;
import com.gsoft.projectManager.model.Role;
import com.gsoft.projectManager.payload.response.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.payload.request.RegistrationRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

public interface AppUserService {
    AppUser signUpUser(AppUser appUser);

    Boolean enableAppUser(String email);

    List<AppUserProfile> getAllAppUsers();

    Boolean deleteAppUser(String username);

    AppUserProfile getAppUserDetails(String username);

    AppUser updateAppUserPatch(String username, RegistrationRequest request);

    AppUser updateAppUser(String username, RegistrationRequest request);

    Boolean updateAppUserPassword(String username, PasswordRequest request);

    Boolean updateAppUserPasswordByAdmin(String username, PasswordRequest request);

    Authentication loginUser(Optional<AppUser> optionalAppUser);

    Boolean checkUsernameAvailability(String username);

    AppUser assignRoles(String username, List<Role> roles);
}
