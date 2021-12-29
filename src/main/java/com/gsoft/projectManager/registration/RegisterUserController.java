package com.gsoft.projectManager.registration;

import com.gsoft.projectManager.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/register")
@AllArgsConstructor
class RegisterUserController {
    private final RegistrationService registrationService;
    private final Logger LOGGER = LoggerFactory.getLogger(RegisterUserController.class);
    
    @PostMapping
    public AppUser register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<?> verifyToken(@RequestParam("token") String token) {
        return new ResponseEntity<>(registrationService.confirmToken(token), HttpStatus.OK);
    }
}