package com.gsoft.projectManager.appuser.login;

import java.util.Optional;

import com.gsoft.projectManager.appuser.AppUser;
import com.gsoft.projectManager.appuser.AppUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    
    private Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/v1/login")
    public ResponseEntity<?> getJwtToken(@RequestParam String usernameOrEmail, @RequestParam String password){
        LOGGER.info("****************=========================================================================");
        LOGGER.info(usernameOrEmail + " " + password);
        Optional<AppUser> optionalUser = appUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if(!optionalUser.isPresent()) return null;
        AppUser user = optionalUser.get();
        LOGGER.info(user.toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), 
                                                                                user.getPassword(),
                                                                                user.getAuthorities());    
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(jwtTokenProvider.generateJwtToken(authentication));
    }

    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello";
    }

}
