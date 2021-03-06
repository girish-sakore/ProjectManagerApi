package com.gsoft.projectManager.repository;

import java.util.List;
import java.util.Optional;

import com.gsoft.projectManager.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    void deleteByEmail(String email);

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByUsernameOrEmail(String username, String email);
  
    List<AppUser>findAll();

    void deleteByUsername(String username);

    Boolean existsByUsername(String username);
}
