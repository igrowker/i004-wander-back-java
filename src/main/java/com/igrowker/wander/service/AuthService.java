
package com.igrowker.wander.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.entity.User;
<<<<<<< HEAD
import com.igrowker.wander.exception.AuthenticationException;
=======
import com.igrowker.wander.repository.UserRepository;
>>>>>>> fe1faf3485ca6bed9a22b7777ae76f677c25d678

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

<<<<<<< HEAD
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;


=======
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
>>>>>>> fe1faf3485ca6bed9a22b7777ae76f677c25d678

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
