package com.gsoft.projectManager.appuser;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String number;

    private String firstName;
    private String lastName;

    private String password;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    private Boolean locked = false;
    private Boolean enabled = false;

    public AppUser(String email,
                   String username,
                   String password,
                   String number,
                   String firstName,
                   String lastName,
                   AppUserRole appUserRole) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.appUserRole = appUserRole;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return !locked;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "" + email + "\n"
                + password + "\n"
                + number + "\n"
                + firstName + "\n"
                + lastName + "\n"
                + appUserRole + "\n"
                + "Enabled:" + enabled + "\n"
                + "Locked:" + locked + "\n";
    }

    public String getAppUserRoleStringFormat() {
        if(appUserRole.equals(AppUserRole.ADMIN)) return "Admin";
        if(appUserRole.equals(AppUserRole.MNGR)) return "Manager";
        if(appUserRole.equals(AppUserRole.DEV)) return "Developer";
        return null;
    }
}
