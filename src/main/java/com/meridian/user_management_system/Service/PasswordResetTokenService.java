package com.meridian.user_management_system.Service;

import com.meridian.user_management_system.Entity.PasswordResetToken;
import com.meridian.user_management_system.Repository.PasswordResetTokenRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public Optional<PasswordResetToken> getToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public void saveToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.save(passwordResetToken);
    }
    
    @Transactional
    public void deleteToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }
}
