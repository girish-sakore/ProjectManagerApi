package com.gsoft.projectManager.appuser;

import java.util.Optional;

import org.springframework.stereotype.Service;

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
}
