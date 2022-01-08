package com.gsoft.projectManager.service;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.gsoft.projectManager.model.AppUser;
import com.gsoft.projectManager.payload.request.RegistrationRequest;
import com.gsoft.projectManager.utils.EmailValidator;
import com.gsoft.projectManager.model.Rolename;
import com.gsoft.projectManager.exception.BadRequestException;
import com.gsoft.projectManager.exception.ResourceNotFoundException;
import com.gsoft.projectManager.payload.response.ConfirmationResponse;
import com.gsoft.projectManager.model.ConfirmationToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new BadRequestException("Email not valid");
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
    public ConfirmationResponse confirmToken(String token) {
        String EMAIL_IS_ALREADY_VERIFIED_MSG = "%s email already verified with %s username";
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new BadRequestException(
                    String.format(EMAIL_IS_ALREADY_VERIFIED_MSG, confirmationToken.getAppUser().getEmail(), confirmationToken.getAppUser().getUsername())
            );
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("token Expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        Boolean isAppUserEnabled = appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );
        if (isAppUserEnabled) {
            LOGGER.info(confirmationToken.getAppUser().getEmail() + " is verified.");
            return new ConfirmationResponse(true, "Your email is Verified.");
        }
        return new ConfirmationResponse(false, "Your email cannot be verified");
    }

}
