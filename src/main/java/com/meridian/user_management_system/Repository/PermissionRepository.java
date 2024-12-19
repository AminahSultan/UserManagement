package com.meridian.user_management_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meridian.user_management_system.Entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Custom method to find permission by name
    Permission findByName(String name);
}
