package com.meridian.user_management_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meridian.user_management_system.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}