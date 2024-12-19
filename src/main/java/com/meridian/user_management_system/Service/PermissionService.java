package com.meridian.user_management_system.Service;

import com.meridian.user_management_system.Entity.Permission;
import com.meridian.user_management_system.Repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Set<Permission> findByIds(Set<Long> permissionIds) {
        Set<Permission> permissions = new HashSet<>();
        for (Long id : permissionIds) {
            permissionRepository.findById(id).ifPresent(permissions::add);
        }
        return permissions;
    }
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Long id, Permission permissionDetails) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setName(permissionDetails.getName());
        return permissionRepository.save(permission);
    }

    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

}
