package com.gsoft.projectManager.registration.token;

import com.gsoft.projectManager.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable=false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }


    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.appUser = appUser ;
    }

    @Override
    public String toString() {
        return "Token: " + token + "\n"
                + "createdAt " + createdAt + "expiredAt" + expiredAt + "\n"
                + appUser.toString();
    }
}
