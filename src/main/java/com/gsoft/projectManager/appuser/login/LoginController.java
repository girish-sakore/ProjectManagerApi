package com.gsoft.projectManager.appuser.login;

import java.util.Optional;

import com.gsoft.projectManager.appuser.AppUser;
import com.gsoft.projectManager.appuser.AppUserRepository;
import com.gsoft.projectManager.appuser.AppUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class LoginController {
    
    private final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private final AppUserRepository appUserRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/api/v1/login")
    public ResponseEntity<?> getJwtToken(@RequestParam String usernameOrEmail, @RequestParam String password){
        Optional<AppUser> optionalUser = appUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        // check password
        if(optionalUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or email!");
        }
        else if(!passwordEncoder.matches(password, optionalUser.get().getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password!");
        }
        Authentication authentication = appUserService.loginUser(optionalUser);
        return ResponseEntity.ok(jwtTokenProvider.generateJwtToken(authentication));
    }
}
