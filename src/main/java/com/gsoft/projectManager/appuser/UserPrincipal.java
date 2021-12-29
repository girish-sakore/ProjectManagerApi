package com.gsoft.projectManager.appuser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private boolean enabled;
    private boolean locked;
    private Collection<? extends GrantedAuthority> authorities;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? new ArrayList<>() : new ArrayList<>(authorities);
    }

    public UserPrincipal(Long id, String firstName, String lastName, String email, String username, String password, Collection<? extends GrantedAuthority> authorities){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.authorities = authorities == null ? new ArrayList<>() : authorities;
    }

    public static UserPrincipal create(AppUser appUser){
        List<GrantedAuthority> authorities = appUser.getRoles().stream()
                                                    .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                                    .collect(Collectors.toList());
        return new UserPrincipal(appUser.getId(), appUser.getFirstName(), appUser.getLastName(), appUser.getEmail(), 
                                 appUser.getUsername(), appUser.getPassword(), authorities);
    }

    public Long getId(){
        return this.id;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(username).append(", ")
                     .append(password).append(", ")
                     .append(email).append(", ")
                     .append(enabled).append(", ")
                     .append(locked).append(", ")
                     .append(authorities);
        return stringBuilder.toString();
    }
}
