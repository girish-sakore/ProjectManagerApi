package com.gsoft.projectManager.registration.token;

import com.gsoft.projectManager.appuser.AppUser;
import com.gsoft.projectManager.appuser.AppUserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AppUserRepository appUserRepository;
//    private final Logger LOGGER = LoggerFactory.getLogger(ConfirmationTokenService.class);

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public Optional<ConfirmationToken> isUserConfirmed(String email) {
        Optional<AppUser> currentUser = appUserRepository.findByEmail(email);
        Optional<ConfirmationToken> token = Optional.empty();
        if(currentUser.isPresent()){
            token = confirmationTokenRepository.findByAppUser(currentUser);
        }
        if(token.isPresent()) { // Token found associated to the current user
            if(token.get().getConfirmedAt() != null){ // Token is confirmed
                return Optional.empty();
            } else { // Token is not confirmed
                return token;
            }
        } else { // Token not found
            throw new ResponseStatusException(HttpStatus.CONFLICT, "AppUser has no entry in ConfirmationToken. To resolve the issue Contact Admin!!");
        }
    }

    public Boolean isTokenExpired(Optional<ConfirmationToken> token) {
        if (token.isPresent() && token.get().getExpiredAt().isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

}
