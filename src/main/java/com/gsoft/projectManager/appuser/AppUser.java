package com.gsoft.projectManager.appuser;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String number;
    private String firstName;
    private String lastName;
    private String password;
    private String username;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"), 
               inverseJoinColumns=@JoinColumn(name="role_id", referencedColumnName="id"))
    private List<Role> roles;

    private Boolean locked = false;
    private Boolean enabled = false;

    public AppUser(String email,
                   String username,
                   String password,
                   String number,
                   String firstName,
                   String lastName,
                   List<Role> roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
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
                + roles.toString() + "\n"
                + "Enabled:" + enabled + "\n"
                + "Locked:" + locked + "\n";

    }
}
