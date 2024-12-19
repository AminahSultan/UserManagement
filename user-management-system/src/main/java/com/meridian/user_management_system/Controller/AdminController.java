package com.meridian.user_management_system.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meridian.user_management_system.DTO.UserRequest;
import com.meridian.user_management_system.Entity.Permission;
import com.meridian.user_management_system.Entity.Role;
import com.meridian.user_management_system.Entity.User;
import com.meridian.user_management_system.Service.UserService;
import com.meridian.user_management_system.Service.PermissionService;
import com.meridian.user_management_system.Service.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public AdminController(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
        // Create a new user
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword()); // Handle password encoding in the service layer

        // Assign roles if provided
        if (userRequest.getRoleIds() != null && !userRequest.getRoleIds().isEmpty()) {
            Set<Role> roles = roleService.findByIds(userRequest.getRoleIds());
            user.setRoles(roles);
        }

        // Assign permissions if provided
        if (userRequest.getPermissionIds() != null && !userRequest.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = permissionService.findByIds(userRequest.getPermissionIds());
            user.setPermissions(permissions);
        }

        // Save the user
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }


    // Get a user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // Update an existing user
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            // Set other fields as needed
            userService.updateUser(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Assign roles to a user
    @PutMapping("/users/{id}/roles")
    public ResponseEntity<User> assignRolesToUser(@PathVariable Long id, @RequestBody Set<Role> roles) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setRoles(roles);
            userService.updateUser(existingUser);
            return ResponseEntity.ok(existingUser);
        }
        return ResponseEntity.notFound().build();
    }

    // Assign permissions to a role
    @PutMapping("/roles/{roleId}/permissions")
    public ResponseEntity<Role> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody Set<Long> permissionIds) {
        Optional<Role> role = roleService.findById(roleId);
        if (role.isPresent()) {
            Role existingRole = role.get();
            Set<Permission> permissions = permissionService.findByIds(permissionIds);
            existingRole.setPermissions(permissions);
            roleService.updateRole(existingRole);
            return ResponseEntity.ok(existingRole);
        }
        return ResponseEntity.notFound().build();
    }
}
