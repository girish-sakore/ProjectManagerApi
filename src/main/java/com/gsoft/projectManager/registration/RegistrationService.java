package com.gsoft.projectManager.registration;

import com.gsoft.projectManager.appuser.AppUser;
import com.gsoft.projectManager.appuser.AppUserRole;
import com.gsoft.projectManager.appuser.AppUserService;
import com.gsoft.projectManager.registration.token.ConfirmationToken;
import com.gsoft.projectManager.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);

    public AppUser register(RegistrationRequest request) {
        boolean isEmailValid = emailValidator.test(request.getEmail());

        if (!isEmailValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email not valid");
        }

        return appUserService.signUpUser(
                new AppUser(
                        request.getEmail(),
                        request.getPassword(),
                        request.getNumber(),
                        request.getFirstName(),
                        request.getLastName(),
                        AppUserRole.DEV
                )
        );
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND ,"Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "Email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "token Expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );
        LOGGER.info(confirmationToken.getAppUser().getEmail() + " User enabled");
        return "Your email is Verified.";
    }

}
