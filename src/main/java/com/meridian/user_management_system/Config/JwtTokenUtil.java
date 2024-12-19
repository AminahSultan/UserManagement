package com.meridian.user_management_system.Config;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtTokenUtil {
    
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // Generate a secure key

    // Generate the JWT Token with roles included
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList());

        return Jwts.builder()
            .setSubject(userDetails.getUsername())  // Extract the username from UserDetails
            .claim("roles", roles)  // Add roles as a claim in the token
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
            .signWith(secretKey)  // Use Key instance instead of plain string
            .compact();
    }

    // Extract username (email or username) from the token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)  // Use Key instance instead of plain string
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    // Extract roles from the token
    public List<String> extractRoles(String token) {
        try {
            // Parse the "roles" claim as a list of strings
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles", List.class);  // Directly fetch the roles as List<String>
        } catch (Exception e) {
            throw new RuntimeException("Error extracting roles from token", e);
        }
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date of the token
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
    }

    // Validate the token by checking the username and expiration
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
