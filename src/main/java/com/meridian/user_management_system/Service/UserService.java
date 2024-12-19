package com.meridian.user_management_system.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.meridian.user_management_system.Entity.Role;
import com.meridian.user_management_system.Entity.User;
import com.meridian.user_management_system.Repository.RoleRepository;
import com.meridian.user_management_system.Repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("USER");
        user.setRoles(new HashSet<>());
        user.getRoles().add(userRole);
        return userRepository.save(user);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email); // Assuming you have a findByEmail method in the UserRepository
    }
    public void updatePassword(User user, String newPassword) {
        // Hash the new password
        String encodedPassword = passwordEncoder.encode(newPassword);
        
        // Set the new password
        user.setPassword(encodedPassword);
        
        // Save the updated user to the database
        userRepository.save(user);
    }
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
