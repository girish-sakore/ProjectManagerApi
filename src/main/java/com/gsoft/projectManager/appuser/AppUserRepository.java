package com.gsoft.projectManager.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

//    @Query("SELECT u FROM AppUser u WHERE u.email = ?1")
    Optional<AppUser> findByEmail(String email);

    List<AppUser>findAll();

    Optional<AppUser> findAppUserByUsername(String username);

    void deleteByUsername(String username);

    Boolean existsAppUserByUsername(String username);
}
