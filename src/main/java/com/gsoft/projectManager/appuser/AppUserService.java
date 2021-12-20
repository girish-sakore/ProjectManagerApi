package com.gsoft.projectManager.appuser;

import com.gsoft.projectManager.registration.token.ConfirmationToken;
import com.gsoft.projectManager.registration.token.ConfirmationTokenRepository;
import com.gsoft.projectManager.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User with email %s not found.";
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final Logger LOGGER = LoggerFactory.getLogger(AppUserService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }
    @Async
    public String signUpUser(AppUser appUser){
        Optional<AppUser> existingUser = appUserRepository.findByEmail(appUser.getEmail());
        if(existingUser.isPresent()){ // User exist
            Optional<ConfirmationToken> currentUserToken = confirmationTokenService.isUserConfirmed(appUser.getEmail());
            if(currentUserToken.isPresent()) { // User token is not confirmed
                if(confirmationTokenService.isTokenExpired(currentUserToken)) { // User token is expired
                    confirmationTokenRepository.deleteById(currentUserToken.get().getId());
                    LOGGER.info("Token was expired and deleted");
                    appUser = existingUser.get();
                } else { // User token not expired
                    return "Email already registered. Check your email for verification";
                }
            } else { // User token already confirmed
                return "Email already taken";
            }
        } else { // User not exist (New User)
            String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
            appUser.setPassword(encodedPassword);
            appUserRepository.save(appUser);
        }
        // Generating token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        LOGGER.info("Token generated");
        return token;
    }

    public void enableAppUser(String email) {
        appUserRepository.enableAppUser(email);
    }
}
