package com.gsoft.projectManager.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService, UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        Optional<AppUser> optionalUser = appUserRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername);
        if(optionalUser.isPresent()) return UserPrincipal.create(optionalUser.get());
        String USER_NOT_FOUND_MSG = "User %s not found";
        throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, emailOrUsername));
    }
}