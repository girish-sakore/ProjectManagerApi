package com.gsoft.projectManager.appuser;

import java.util.List;

import com.gsoft.projectManager.payload.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.registration.RegistrationRequest;

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
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/appUsers")
public class AppUserController {
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

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

    @PostMapping("/admin/{username}/changeRoles")
    public ResponseEntity<?> changeRoles(@PathVariable String username, @RequestBody List<Role> roles){
        AppUser appUser = appUserRepository.findByUsername(username)
                                           .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        appUser.setRoles(roles);
        return ResponseEntity.ok(appUserRepository.save(appUser));
    }
}
