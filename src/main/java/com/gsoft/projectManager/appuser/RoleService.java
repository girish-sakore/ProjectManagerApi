package com.gsoft.projectManager.appuser;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoleService {
    
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepo){
        this.roleRepository = roleRepo;
    }

    public Role findOrCreateRole(Rolename name){
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if(optionalRole.isPresent()) return optionalRole.get();
        Role newRole = new Role(name);
        return roleRepository.save(newRole);
    }

    public Role findRoleByName(Rolename name){
        return roleRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));
    }

    public Role addRole(Rolename name){
        boolean isRolePresent = roleRepository.findByName(name).isPresent();
        if(isRolePresent){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already exists!");
        }
        return roleRepository.save(new Role(name));
    }
}
