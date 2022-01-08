package com.gsoft.projectManager.service.serviceImpl;

import com.gsoft.projectManager.model.AppUser;
import com.gsoft.projectManager.repository.AppUserRepository;
import com.gsoft.projectManager.model.UserPrincipal;
import com.gsoft.projectManager.service.CustomUserDetailsService;
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
        throw new UsernameNotFoundException(emailOrUsername);
    }
}