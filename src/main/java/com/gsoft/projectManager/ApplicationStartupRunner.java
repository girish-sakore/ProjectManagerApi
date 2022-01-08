package com.gsoft.projectManager;

import java.util.EnumSet;

import com.gsoft.projectManager.service.RoleService;
import com.gsoft.projectManager.model.Rolename;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements CommandLineRunner{
    
    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        EnumSet.allOf(Rolename.class).forEach(roleName -> roleService.findOrCreateRole(roleName));
    }
}
