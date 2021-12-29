package com.gsoft.projectManager.appuser;

import com.gsoft.projectManager.mailer.EmailSender;
import com.gsoft.projectManager.mailer.EmailService;
import com.gsoft.projectManager.payload.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.registration.RegistrationRequest;
import com.gsoft.projectManager.registration.token.ConfirmationToken;
import com.gsoft.projectManager.registration.token.ConfirmationTokenRepository;
import com.gsoft.projectManager.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final static String USER_NOT_FOUND_MSG = "User with username %s not found.";
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final EmailSender emailSender;

    private final Logger LOGGER = LoggerFactory.getLogger(AppUserService.class);

    @Override
    @Async
    public AppUser signUpUser(AppUser appUser) {
        Optional<AppUser> existingUser = appUserRepository.findByEmail(appUser.getEmail());
        AppUser newUser;
        if (existingUser.isPresent()) { // User exist
            Optional<ConfirmationToken> currentUserToken = confirmationTokenService.isUserConfirmed(appUser.getEmail());
            if (currentUserToken.isPresent()) { // User token is not confirmed
                if (confirmationTokenService.isTokenExpired(currentUserToken)) { // User token is expired
                    confirmationTokenRepository.deleteById(currentUserToken.get().getId());
                    LOGGER.info("Token was expired and deleted");
                    newUser = existingUser.get();
                } else { // User token not expired
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered. Check your email for verification");
                }
            } else { // User token already confirmed
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already taken");
            }
        } else { // User not exist (New User)
            String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
            appUser.setPassword(encodedPassword);
            newUser = appUserRepository.save(appUser);
        }
        // Generate token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                newUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        LOGGER.info("Token generated: " + token);
        String baseURI = "http://localhost:3000";
        String link = baseURI + "/api/v1/register/confirm?token=" + token;
        emailSender.send(newUser.getEmail(),
                emailService.buildEmail(
                        newUser.getFirstName(),
                        link
                )
        );
        LOGGER.info("Registration email sent");
        return newUser;
    }

    @Override
    public Boolean enableAppUser(String email) {
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_MSG, email))
                );
        appUser.setEnabled(true);
        return true;
    }

    @Override
    public Boolean deleteAppUser(String username) {
        if(appUserRepository.existsAppUserByUsername(username))
            appUserRepository.deleteByUsername(username);
        return true;
    }

    private AppUserProfile setAppUserProfile(AppUser appUser) {
        return new AppUserProfile(
                appUser.getUsername(),
                appUser.getEmail(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getNumber(),
                appUser.getAppUserRoleStringFormat()
        );
    }

    @Override
    public AppUserProfile getAppUserDetails(String username) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_MSG, username))
                );
        return setAppUserProfile(appUser);
    }

    @Override
    public List<AppUserProfile> getAllAppUsers() {
        List<AppUserProfile> response = new ArrayList< >();
        appUserRepository.findAll()
                    .forEach(appUser -> response.add(setAppUserProfile(appUser)));
        return response;
    }

    @Override
    public AppUser updateAppUser(String username, RegistrationRequest request) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_MSG, username)));

        appUser.setUsername(request.getUsername());
        appUser.setEmail(request.getEmail());
        appUser.setNumber(request.getNumber());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser updateAppUserPatch(String username, RegistrationRequest request) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_MSG, username)));

        if (request.getUsername() != null) appUser.setUsername(request.getUsername());
        if (request.getEmail() != null) appUser.setEmail(request.getEmail());
        if (request.getFirstName() != null) appUser.setFirstName(request.getFirstName());
        if (request.getLastName() != null) appUser.setLastName(request.getLastName());
        if (request.getNumber() != null) appUser.setNumber(request.getNumber());
        return appUserRepository.save(appUser);
    }

    @Override
    public Boolean updateAppUserPassword(String username, PasswordRequest request) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_MSG, username)));

        if (bCryptPasswordEncoder.matches(request.getOldPassword(), appUser.getPassword())) {
            String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
            appUser.setPassword(encodedPassword);
            appUserRepository.save(appUser);
            return true;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
    }

    @Override
    public Boolean updateAppUserPasswordByAdmin(String username, PasswordRequest request) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(USER_NOT_FOUND_MSG, username)));
        String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        appUser.setPassword(encodedPassword);
        return true;
    }


}
