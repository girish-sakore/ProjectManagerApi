package com.gsoft.projectManager.appuser;

import com.gsoft.projectManager.payload.AppUserProfile;
import com.gsoft.projectManager.payload.request.PasswordRequest;
import com.gsoft.projectManager.registration.RegistrationRequest;
import java.util.List;
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

public interface AppUserService {
    AppUser signUpUser(AppUser appUser);


    Boolean enableAppUser(String email);

    List<AppUserProfile> getAllAppUsers();

    Boolean deleteAppUser(String username);

    AppUserProfile getAppUserDetails(String username);

    AppUser updateAppUserPatch(String username, RegistrationRequest request);

    AppUser updateAppUser(String username, RegistrationRequest request);

    Boolean updateAppUserPassword(String username, PasswordRequest request);

    Boolean updateAppUserPasswordByAdmin(String username, PasswordRequest request);


    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        Optional<AppUser> optionalUser = appUserRepository.findByUsername(emailOrUsername);
        if(optionalUser.isPresent()) return UserPrincipal.create(optionalUser.get());
        optionalUser = appUserRepository.findByEmail(emailOrUsername);
        if(optionalUser.isPresent()) return UserPrincipal.create(optionalUser.get());
        throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, emailOrUsername));
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
