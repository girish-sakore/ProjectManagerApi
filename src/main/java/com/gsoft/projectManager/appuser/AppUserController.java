package com.gsoft.projectManager.appuser;

import com.gsoft.projectManager.payload.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/appUsers")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/")
    public ResponseEntity<?> allAppUsers() {
        List<AppUserProfile> response = appUserService.getAllAppUsers();
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> appUserDetails(@PathVariable String username) {
        AppUserProfile response = appUserService.getAppUserDetails(username);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<?> updateAppUserPartial(@PathVariable String username, @RequestBody RegistrationRequest request) {
        AppUser response = appUserService.updateAppUserPatch(username, request);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateAppUser(@PathVariable String username, @RequestBody RegistrationRequest request) {
        AppUser response = appUserService.updateAppUser(username, request);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteAppUser(@PathVariable String username) {
        appUserService.deleteAppUser(username);
        return new ResponseEntity< >(String.format("User with username %s is Deleted.", username), HttpStatus.ACCEPTED);
    }

    @PostMapping("/{username}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable String username, @RequestBody PasswordRequest request) {
        Boolean response = appUserService.updateAppUserPassword(username,request);
        return new ResponseEntity< > (response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{username}/changePassword")
    public ResponseEntity<?> changePasswordByAdmin(@PathVariable String username, @RequestBody PasswordRequest request) {
        Boolean response = appUserService.updateAppUserPasswordByAdmin(username, request);
        return new ResponseEntity< > (response, HttpStatus.OK);
    }



}
