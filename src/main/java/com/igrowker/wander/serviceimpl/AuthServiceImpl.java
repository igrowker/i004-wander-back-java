package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.user.*;
import com.igrowker.wander.entity.RevokedToken;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.*;
import com.igrowker.wander.repository.RevokedTokenRepository;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.AuthService;
import com.igrowker.wander.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

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

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public ResponseUserDto registerUser(@Valid RegisterUserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResourceAlreadyExistsException("El correo " + userDto.getEmail() + " ya está registrado.");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setPreferences(new ArrayList<>());
        user.setLocation(userDto.getLocation());
        user.setCreatedAt(new Date());

        user.setEnabled(false);
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000));

        emailService.sendVerificationEmail(user);

        User savedUser = userRepository.save(user);
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(savedUser.getId());
        responseUserDto.setName(savedUser.getName());
        responseUserDto.setPhone(savedUser.getPhone());
        responseUserDto.setEmail(savedUser.getEmail());
        responseUserDto.setRole(savedUser.getRole());
        responseUserDto.setPreferences(savedUser.getPreferences());
        responseUserDto.setLocation(savedUser.getLocation());
        responseUserDto.setCreatedAt(savedUser.getCreatedAt());
        responseUserDto.setVerificationCode(savedUser.getVerificationCode());

        return responseUserDto;
    }

    public ResponseUserDto verifyUser(@Valid RequestVerifyUserDto verifyUserDto) {
        Optional<User> optionalUser = userRepository.findByEmail(verifyUserDto.getEmail());

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }

        User user = optionalUser.get();

        if (user.getVerificationCode() == null) {
            throw new InvalidDataException("Cuenta ya se encuentra verificada.");
        }

        // Cambio aquí: LocalDateTime.now() -> new Date()
        if (user.getVerificationCodeExpiresAt().before(new Date())) {
            throw new InvalidDataException("Código de verificación vencido.");
        }

        if (!user.getVerificationCode().equals(verifyUserDto.getVerificationCode())) {
            throw new InvalidDataException("Código de verificación incorrecto.");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        User savedUser = userRepository.save(user);

        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(savedUser.getId());
        responseUserDto.setName(savedUser.getName());
        responseUserDto.setEmail(savedUser.getEmail());
        responseUserDto.setRole(savedUser.getRole());
        responseUserDto.setPreferences(savedUser.getPreferences());
        responseUserDto.setLocation(savedUser.getLocation());
        responseUserDto.setCreatedAt(savedUser.getCreatedAt());
        responseUserDto.setVerificationCode(null);

        return responseUserDto;
    }

    @Transactional
    @Override
    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

        if (user.isEnabled()) {
            throw new InvalidDataException("La cuenta ya está verificada.");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000));  // Cambio aquí: LocalDateTime.now().plusMinutes(15) -> Date

        userRepository.save(user);
        emailService.sendVerificationEmail(user);
    }

    @Transactional
    @Override
    public LoginResponse authenticateUser(@Valid LoginRequest loginRequest) {
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

        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .preferences(user.getPreferences())
                .location(user.getLocation())
                .build();

        return LoginResponse.builder()
                .idUser(user.getId())
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .user(userDto)
                .build();
    }

    @Override
    public String logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidJwtException("Invalid Authorization");
        }

        try {

            String token = authorizationHeader.substring(7);

            jwtService.extractAllClaims(token);

            invalidateToken(token, jwtService.extractExpiration(token));

            // Success response
            return "Token invalidated successfully.";
        } catch (Exception e) {
            // Handle errors related to the token
            throw new InvalidJwtException("Error processing the token: " + e.getMessage());
        }

    }

    @Transactional
    @Override
    public void sendForgotPasswordEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setPasswordResetCode(generateVerificationCode());
        user.setPasswordResetCodeExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000));  // Cambio aquí: LocalDateTime.now().plusMinutes(15) -> Date
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user);
    }

    @Transactional
    @Override
    public void resetPassword(String mail, String code, String newPassword) {

        User user = userRepository.findByEmail(mail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (user.getPasswordResetCode() == null) {
            throw new InvalidDataException("Cuenta ya se encuentra verificada.");
        }

        // Cambio aquí: LocalDateTime.now() -> new Date()
        if (user.getPasswordResetCodeExpiresAt().before(new Date())) {
            throw new InvalidDataException("Código de verificación vencido.");
        }

        if (!user.getPasswordResetCode().equals(code)) {
            throw new InvalidDataException("Código de verificación incorrecto.");
        }

        user.setEnabled(true);
        user.setPasswordResetCode(null);
        user.setPasswordResetCodeExpiresAt(null);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void invalidateToken(String token, Date expirationDate) {
        if (!revokedTokenRepository.existsByToken(token)) {
            revokedTokenRepository.save(new RevokedToken(token, expirationDate));
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}