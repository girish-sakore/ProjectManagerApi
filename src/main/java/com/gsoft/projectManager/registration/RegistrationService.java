package com.gsoft.projectManager.registration;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.gsoft.projectManager.appuser.AppUser;
import com.gsoft.projectManager.appuser.AppUserService;
import com.gsoft.projectManager.appuser.RoleService;
import com.gsoft.projectManager.appuser.Rolename;
import com.gsoft.projectManager.registration.token.ConfirmationToken;
import com.gsoft.projectManager.registration.token.ConfirmationTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final RoleService roleService;

    public final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);

    public AppUser register(RegistrationRequest request) {
        boolean isEmailValid = emailValidator.test(request.getEmail());

        if (!isEmailValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not valid");
        }

        return appUserService.signUpUser(
                new AppUser(
                        request.getEmail(),
                        request.getUsername(),
                        request.getPassword(),
                        request.getNumber(),
                        request.getFirstName(),
                        request.getLastName(),
                        Arrays.asList(roleService.findRoleByName(Rolename.ROLE_USER))
                )
        );
    }

    @Transactional
    public String confirmToken(String token) {
        String EMAIL_IS_ALREADY_VERIFIED_MSG = "%s email already verified with %s username";
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.ALREADY_REPORTED,
                    String.format(EMAIL_IS_ALREADY_VERIFIED_MSG, confirmationToken.getAppUser().getEmail(), confirmationToken.getAppUser().getUsername())
            );
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "token Expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        Boolean isAppUserEnabled = appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );
        if (isAppUserEnabled) {
            LOGGER.info(confirmationToken.getAppUser().getEmail() + " is verified.");
            return "Your email is Verified.";
        }
        return "Your email cannot be verified";
    }

}
