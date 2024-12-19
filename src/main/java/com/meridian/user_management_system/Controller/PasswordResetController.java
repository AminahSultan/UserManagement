package com.meridian.user_management_system.Controller;

import com.meridian.user_management_system.Entity.PasswordResetToken;
import com.meridian.user_management_system.Entity.User;
import com.meridian.user_management_system.Service.PasswordResetTokenService;
import com.meridian.user_management_system.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/password-reset")
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordResetController(UserService userService, PasswordResetTokenService passwordResetTokenService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    // Endpoint to request a password reset
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam("email") String email) {
        // Check if the email exists in the system
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        // Generate a password reset token and save it
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(generateRandomToken()); // Method to generate a random token (you can implement this)
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour

        passwordResetTokenService.saveToken(token);

        // In a real scenario, you would send an email with the reset token to the user
        // Here, we'll just return a success message for now
        return new ResponseEntity<>("Password reset token sent successfully", HttpStatus.OK);
    }

    // Endpoint to reset the password using the token
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,
                                                @RequestParam("newPassword") String newPassword) {
        // Find the password reset token in the database
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenService.getToken(token);
        if (passwordResetTokenOptional.isEmpty()) {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }

        PasswordResetToken passwordResetToken = passwordResetTokenOptional.get();

        // Check if the token has expired
        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("Token has expired", HttpStatus.BAD_REQUEST);
        }

        // Reset the user's password
        User user = passwordResetToken.getUser();
        userService.updatePassword(user, newPassword); // Update the user's password in the database

        // Optionally delete the token after use
        passwordResetTokenService.deleteToken(token);

        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }

    // Helper method to generate a random token (for simplicity, use UUID in this case)
    private String generateRandomToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
