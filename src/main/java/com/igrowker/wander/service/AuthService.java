package com.igrowker.wander.service;

import com.igrowker.wander.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.AuthenticationException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;



    public String authenticateUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
         if (userOptional.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
         return generateToken(userOptional.get());
        }
        throw new AuthenticationException("Invalid email or password");
    }

    private String generateToken(User user) {
    return Jwts.builder()
            .setSubject(user.getEmail()) // mail for main claim
            .claim("userId", user.getId()) // ID user
            .claim("roles", user.getRole()) // Rol user
            .setIssuedAt(new Date()) // 
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) 
            .signWith(SignatureAlgorithm.HS256, secretKey) 
            .compact();
}

}
