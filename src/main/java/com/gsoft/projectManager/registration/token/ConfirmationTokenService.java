package com.gsoft.projectManager.registration.token;

import com.gsoft.projectManager.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(ConfirmationTokenService.class);

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public Optional<ConfirmationToken> findConfirmedAppUser(AppUser appUser){
        Optional<ConfirmationToken> optionalToken = confirmationTokenRepository.findByAppUser(appUser);
        if(optionalToken.isPresent()){
            LOGGER.warn("======================================");
            if(optionalToken.get().getConfirmedAt() == null){
                LOGGER.warn("-------findConfirmedAppUser-------" + optionalToken.toString() + "------------");
                return Optional.empty();
            }
        }
        return optionalToken;
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

}
