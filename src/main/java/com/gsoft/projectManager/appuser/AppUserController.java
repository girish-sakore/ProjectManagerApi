package com.gsoft.projectManager.appuser;

import com.gsoft.projectManager.payload.response.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.registration.RegistrationRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/appUsers")
public class AppUserController {
    private final AppUserService appUserService;
    private final Logger LOGGER = LoggerFactory.getLogger(AppUserController.class);

    @PreAuthorize("hasRole('ADMIN')") // maybe add some other roles
    @GetMapping("/")
    public ResponseEntity<?> allAppUsers() {
        List<AppUserProfile> response = appUserService.getAllAppUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or principal == #username")
    @GetMapping("/{username}")
    public ResponseEntity<?> appUserDetails(@PathVariable String username) {
        AppUserProfile response = appUserService.getAppUserDetails(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or principal == #username")
    @PatchMapping("/{username}")
    public ResponseEntity<?> updateAppUserPartial(@PathVariable String username, @RequestBody RegistrationRequest request) {
        AppUser response = appUserService.updateAppUserPatch(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or principal == #username")
    @PutMapping("/{username}")
    public ResponseEntity<?> updateAppUser(@PathVariable String username, @RequestBody RegistrationRequest request) {
        AppUser response = appUserService.updateAppUser(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or principal == #username")
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteAppUser(@PathVariable String username) {
        if (appUserService.deleteAppUser(username))
            return new ResponseEntity<>(String.format("User with username %s is Deleted.", username), HttpStatus.ACCEPTED);
        return new ResponseEntity<>("Unexpected error.", HttpStatus.EXPECTATION_FAILED);
    }

    @PreAuthorize("principal == #username")
    @PostMapping("/{username}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable String username, @RequestBody PasswordRequest request) {
        Boolean response = appUserService.updateAppUserPassword(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{username}/changePassword")
    public ResponseEntity<?> changePasswordByAdmin(@PathVariable String username, @RequestBody PasswordRequest request) {
        Boolean response = appUserService.updateAppUserPasswordByAdmin(username, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/admin/{username}/assignRoles")
    public ResponseEntity<?> assignRoles(@PathVariable String username, @RequestBody List<Role> roles) {
        LOGGER.warn(roles.toString());
        AppUser response = appUserService.assignRoles(username, roles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
        Boolean response = appUserService.checkUsernameAvailability(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
