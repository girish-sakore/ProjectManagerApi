package com.gsoft.projectManager.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/register")
@AllArgsConstructor
class RegisterUserController {
    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String verifyToken(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}