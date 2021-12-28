package com.gsoft.projectManager.appuser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // @Query("SELECT u FROM AppUser u WHERE u.email = ?1")
    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " + "SET a.enabled = TRUE WHERE a.email = ?1")
    void enableAppUser(String email);

    void deleteByEmail(String email);

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByUsernameOrEmail(String username, String email);
}
