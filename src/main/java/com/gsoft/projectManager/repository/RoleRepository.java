package com.gsoft.projectManager.repository;

import java.util.Optional;

import com.gsoft.projectManager.model.Role;
import com.gsoft.projectManager.model.Rolename;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{
    Optional<Role> findByName(Rolename name);
}
