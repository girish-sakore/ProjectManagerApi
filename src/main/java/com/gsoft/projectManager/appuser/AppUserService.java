package com.gsoft.projectManager.appuser;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.gsoft.projectManager.mailer.EmailSender;
import com.gsoft.projectManager.mailer.EmailService;
import com.gsoft.projectManager.registration.token.ConfirmationToken;
import com.gsoft.projectManager.registration.token.ConfirmationTokenRepository;
import com.gsoft.projectManager.registration.token.ConfirmationTokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User with email %s not found.";
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final EmailSender emailSender;

    private final Logger LOGGER = LoggerFactory.getLogger(AppUserService.class);

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        Optional<AppUser> optionalUser = appUserRepository.findByUsername(emailOrUsername);
        if(optionalUser.isPresent()) return UserPrincipal.create(optionalUser.get());
        optionalUser = appUserRepository.findByEmail(emailOrUsername);
        if(optionalUser.isPresent()) return UserPrincipal.create(optionalUser.get());
        throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, emailOrUsername));
    }

    @Async
    public AppUser signUpUser(AppUser appUser) {
        Optional<AppUser> existingUser = appUserRepository.findByEmail(appUser.getEmail());
        AppUser newUser = new AppUser();
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
        String baseURI = "http://localhost:3000"; // temp
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

    public void enableAppUser(String email) {
        appUserRepository.enableAppUser(email);
    }

    public Authentication loginUser(Optional<AppUser> optionalAppUser){
        if(optionalAppUser.isPresent()){
            AppUser appUser = optionalAppUser.get();
            if(!appUser.isEnabled()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User account is disabled!");
            }
            else if(!appUser.isAccountNonLocked()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User account is locked!");
            }
            else if(!appUser.isAccountNonExpired()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User account is expired!");
            }
            else if(!appUser.isCredentialsNonExpired()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User account credentials are expired!");
            }
            UserPrincipal user = UserPrincipal.create(optionalAppUser.get());
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), 
                                                                                    user.getPassword(),
                                                                                    user.getAuthorities());    
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.info("User signed in successfully!");
            return authentication;
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to login!");
    }
}
