package com.meridian.user_management_system.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meridian.user_management_system.Entity.PasswordResetToken;
import com.meridian.user_management_system.Entity.User;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Find a password reset token by the token value
    Optional<PasswordResetToken> findByToken(String token);

    // Find a password reset token by the associated user
    Optional<PasswordResetToken> findByUser(User user);

    // Optional: Delete by token (could be useful for cleanup)
    void deleteByToken(String token);
}
