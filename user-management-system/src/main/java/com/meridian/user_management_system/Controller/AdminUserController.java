package com.meridian.user_management_system.Controller;

import com.meridian.user_management_system.Entity.Permission;
import com.meridian.user_management_system.Entity.Role;
import com.meridian.user_management_system.Entity.User;
import com.meridian.user_management_system.Service.PermissionService;
import com.meridian.user_management_system.Service.RoleService;
import com.meridian.user_management_system.Service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v2/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public AdminUserController(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    // Assign roles to a user
    @PutMapping("/{userId}/roles")
    public ResponseEntity<User> assignRolesToUser(@PathVariable Long userId, @RequestBody Set<Long> roleIds) {
        Optional<User> optionalUser = userService.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Role> roles = roleService.findByIds(roleIds);

            user.setRoles(roles);
            userService.updateUser(user);

            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // Assign permissions directly to a user
    @PutMapping("/{userId}/permissions")
    public ResponseEntity<User> assignPermissionsToUser(@PathVariable Long userId, @RequestBody Set<Long> permissionIds) {
        Optional<User> optionalUser = userService.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Permission> permissions = permissionService.findByIds(permissionIds);

            user.setPermissions(permissions);
            userService.updateUser(user);

            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}
