package com.gsoft.projectManager.appuser.login;

import java.util.Optional;

import com.gsoft.projectManager.appuser.AppUser;
import com.gsoft.projectManager.appuser.AppUserRepository;
import com.gsoft.projectManager.appuser.UserPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LoginController {
    
    private Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/v1/login")
    public ResponseEntity<?> getJwtToken(@RequestParam String usernameOrEmail, @RequestParam String password){
        LOGGER.info("****************===================================Logging in======================================");
        LOGGER.info(usernameOrEmail + " " + password);
        Optional<AppUser> optionalUser = appUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if(!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid username or password");
        }
        UserPrincipal user = UserPrincipal.create(optionalUser.get());
        LOGGER.info(user.toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), 
                                                                                user.getPassword(),
                                                                                user.getAuthorities());    
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.info("Generating json web token");
        return ResponseEntity.ok(jwtTokenProvider.generateJwtToken(authentication));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/testHello")
    public String sayHello(){
        LOGGER.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return "hello guys!";
    }
}
