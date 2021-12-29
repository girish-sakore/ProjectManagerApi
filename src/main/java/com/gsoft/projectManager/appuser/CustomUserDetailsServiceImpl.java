package com.gsoft.projectManager.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final String USER_NOT_FOUND_MSG = "User with username %s not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         return (UserDetails) appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));
    }
}