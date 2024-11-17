package com.igrowker.wander.service;

import com.igrowker.wander.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String secretKey = "claveSecretaMuySegura"; // Cambia esto por una clave segura

    public String authenticateUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
         if (userOptional.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
         return generateToken(userOptional.get());
        }
        return null;
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Cambia `getUsername` a `getEmail` si es necesario
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
