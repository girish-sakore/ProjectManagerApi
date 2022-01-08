package com.gsoft.projectManager.controller;

import com.gsoft.projectManager.model.AppUser;

import com.gsoft.projectManager.payload.response.ConfirmationResponse;
import com.gsoft.projectManager.payload.request.RegistrationRequest;
import com.gsoft.projectManager.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/register")
@AllArgsConstructor
class RegisterUserController {
    private final RegistrationService registrationService;
    private final Logger LOGGER = LoggerFactory.getLogger(RegisterUserController.class);

    @PostMapping
    public AppUser register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<?> verifyToken(@RequestParam("token") String token) {
        ConfirmationResponse response = registrationService.confirmToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}