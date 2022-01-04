package com.gsoft.projectManager.appuser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.gsoft.projectManager.exception.*;
import com.gsoft.projectManager.mailer.EmailSender;
import com.gsoft.projectManager.mailer.EmailService;
import com.gsoft.projectManager.payload.response.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.registration.RegistrationRequest;
import com.gsoft.projectManager.registration.token.ConfirmationToken;
import com.gsoft.projectManager.registration.token.ConfirmationTokenRepository;
import com.gsoft.projectManager.registration.token.ConfirmationTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final EmailSender emailSender;
    private final RoleService roleService;

    private final Logger LOGGER = LoggerFactory.getLogger(AppUserService.class);

    public Authentication loginUser(Optional<AppUser> optionalAppUser) {
        if (optionalAppUser.isPresent()) {
            AppUser appUser = optionalAppUser.get();
            if (!appUser.isEnabled()) {
                throw new UnauthorizedException("User account is disabled!");
            } else if (!appUser.isAccountNonLocked()) {
                throw new UnauthorizedException("User account is locked!");
            } else if (!appUser.isAccountNonExpired()) {
                throw new UnauthorizedException("User account is expired!");
            } else if (!appUser.isCredentialsNonExpired()) {
                throw new UnauthorizedException("User account credentials are expired!");
            }
            UserPrincipal user = UserPrincipal.create(optionalAppUser.get());
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.info("User authenticated!");
            return authentication;
        }
        throw new InternalServerException("Unable to login!");
    }

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
                    throw new BadRequestException("Email already registered. Check your email for verification");
                }
            } else { // User token already confirmed
                throw new BadRequestException("Email already taken");
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
                .orElseThrow(() -> new UserNotFoundException(email)
                );
        appUser.setEnabled(true);
        return true;
    }

    @Override
    public Boolean deleteAppUser(String username) {
        if (appUserRepository.existsByUsername(username))
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
                appUser.getRoles()
        );
    }

    @Override
    public AppUserProfile getAppUserDetails(String username) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)
                );
        return setAppUserProfile(appUser);
    }

    @Override
    public List<AppUserProfile> getAllAppUsers() {
        List<AppUserProfile> response = new ArrayList<>();
        appUserRepository.findAll()
                .forEach(appUser -> response.add(setAppUserProfile(appUser)));
        return response;
    }

    @Override
    public AppUser updateAppUser(String username, RegistrationRequest request) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        appUser.setUsername(request.getUsername());
        appUser.setEmail(request.getEmail());
        appUser.setNumber(request.getNumber());
        appUser.setFirstName(request.getFirstName());
        appUser.setLastName(request.getLastName());
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser updateAppUserPatch(String username, RegistrationRequest request) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (request.getUsername() != null) appUser.setUsername(request.getUsername());
        if (request.getEmail() != null) appUser.setEmail(request.getEmail());
        if (request.getFirstName() != null) appUser.setFirstName(request.getFirstName());
        if (request.getLastName() != null) appUser.setLastName(request.getLastName());
        if (request.getNumber() != null) appUser.setNumber(request.getNumber());
        return appUserRepository.save(appUser);
    }

    @Override
    public Boolean updateAppUserPassword(String username, PasswordRequest request) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (bCryptPasswordEncoder.matches(request.getOldPassword(), appUser.getPassword())) {
            String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
            appUser.setPassword(encodedPassword);
            appUserRepository.save(appUser);
            return true;
        }
        throw new BadRequestException("Old password is incorrect");
    }

    @Override
    public Boolean updateAppUserPasswordByAdmin(String username, PasswordRequest request) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        String encodedPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        return true;
    }

    @Override
    public AppUser assignRoles(String username, List<Role> roles) {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
//        appUser.setRoles(roles);
        List<Role> newRoles = new ArrayList<>();
        roles.forEach(role -> {
            newRoles.add(roleService.findOrCreateRole(role.getName()));
        });
        appUser.setRoles(newRoles);
        return appUserRepository.save(appUser);
    }

    @Override
    public Boolean checkUsernameAvailability(String username) {
        if (username.trim().length() > 4) {
            return !appUserRepository.existsByUsername(username);
        } else {
            throw new BadRequestException("username is too short.");
        }
    }

}
