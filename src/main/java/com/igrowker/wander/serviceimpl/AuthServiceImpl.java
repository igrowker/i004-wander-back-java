package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.dto.LoginResponse;
import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.entity.RevokedToken;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidJwtException;
import com.igrowker.wander.exception.InvalidUserCredentialsException;
import com.igrowker.wander.exception.ResourceAlreadyExistsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.RevokedTokenRepository;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.AuthService;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // email

    @Transactional
    public ResponseUserDto registerUser(RegisterUserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email " + userDto.getEmail() + " already exists");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setPreferences(new ArrayList<>());
        user.setLocation(userDto.getLocation());
        user.setCreatedAt(LocalDateTime.now());
        user.setEnabled(true);

        User saveUser = userRepository.save(user);
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(saveUser.getId());
        responseUserDto.setName(saveUser.getName());
        responseUserDto.setEmail(saveUser.getEmail());
        responseUserDto.setRole(saveUser.getRole());
        responseUserDto.setPreferences(saveUser.getPreferences());
        responseUserDto.setLocation(saveUser.getLocation());
        responseUserDto.setCreatedAt(saveUser.getCreatedAt());

        return responseUserDto;
    }

    @Transactional
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Usuario no encontrado."));

        if (!user.isEnabled()) {
            throw new InvalidUserCredentialsException("Cuenta no verificada. Por favor verifique su cuenta.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidUserCredentialsException("Email y/o contraseña inválidos.");
        }

        String jwtToken = jwtService.generateToken(user);

        return LoginResponse.builder()
                .idUser(user.getId())
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    public String logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidJwtException("Invalid Authorization");
        }

        try {
            // Extract the token from the header
            String token = authorizationHeader.substring(7);

            // Validate the token using JwtService
            jwtService.extractAllClaims(token); // Throws an exception if the token is invalid

            // Invalidate the token by storing it in the revoked tokens repository
            invalidateToken(token, jwtService.extractExpiration(token));

            // Success response
            return "Token invalidated successfully.";
        } catch (Exception e) {
            // Handle errors related to the token
            throw new InvalidJwtException("Error processing the token: " + e.getMessage());
        }

    }

    private void invalidateToken(String token, Date expirationDate) {
        if (!revokedTokenRepository.existsByToken(token)) {
            revokedTokenRepository.save(new RevokedToken(token, expirationDate));
        }
    }

}
