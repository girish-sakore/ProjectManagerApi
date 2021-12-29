package com.gsoft.projectManager.appuser;

import com.gsoft.projectManager.payload.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.registration.RegistrationRequest;
import java.util.List;

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

}
