package com.meridian.user_management_system.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.meridian.user_management_system.Entity.Permission;
import com.meridian.user_management_system.Entity.Role;
import com.meridian.user_management_system.Entity.User;
import com.meridian.user_management_system.Repository.PermissionRepository;
import com.meridian.user_management_system.Repository.RoleRepository;
import com.meridian.user_management_system.Repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, 
                      PermissionRepository permissionRepository, 
                      UserRepository userRepository, 
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create or fetch permissions
        Permission readPermission = permissionRepository.findByName("READ");
        if (readPermission == null) {
            readPermission = new Permission();
            readPermission.setName("READ");
            permissionRepository.save(readPermission);
        }

        Permission writePermission = permissionRepository.findByName("WRITE");
        if (writePermission == null) {
            writePermission = new Permission();
            writePermission.setName("WRITE");
            permissionRepository.save(writePermission);
        }

        // Create or fetch the ADMIN role
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setPermissions(new HashSet<>(Arrays.asList(readPermission, writePermission)));
            roleRepository.save(adminRole);
        }

        // Create or fetch the USER role
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            userRole.setPermissions(new HashSet<>(Collections.singletonList(readPermission)));
            roleRepository.save(userRole);
        }

     // Create the admin user if it doesn't exist
        Optional<User> adminUserOptional = userRepository.findByEmail("meridianTechsol@meridianit.com");
        if (adminUserOptional.isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("meridianit");
            adminUser.setEmail("meridianTechsol@meridianit.com");
            adminUser.setPassword(passwordEncoder.encode("meridianit")); // Encrypt the password
            adminUser.setRoles(new HashSet<>(Collections.singletonList(adminRole))); // Assign ADMIN role
            userRepository.save(adminUser);
        }

    }
}
