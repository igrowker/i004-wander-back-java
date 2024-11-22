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
import com.igrowker.wander.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
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


    @Transactional
    public ResponseUserDto registerUser(@Valid RegisterUserDto userDto) {
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

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));

        sendVerificationEmail(user);

        User savedUser = userRepository.save(user);
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(savedUser.getId());
        responseUserDto.setName(savedUser.getName());
        responseUserDto.setEmail(savedUser.getEmail());
        responseUserDto.setRole(savedUser.getRole());
        responseUserDto.setPreferences(savedUser.getPreferences());
        responseUserDto.setLocation(savedUser.getLocation());
        responseUserDto.setCreatedAt(savedUser.getCreatedAt());
        responseUserDto.setVerificationCode(savedUser.getVerificationCode());

        return responseUserDto;
    }

    @Transactional
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

    public ResponseUserDto verifyUser(RequestVerifyUserDto verifyUserDto) {
        Optional<User> optionalUser = userRepository.findByEmail(verifyUserDto.getEmail());

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Usuario no encontrado.");
        }

        User user = optionalUser.get();

        if (user.getVerificationCode() == null) {
            throw new InvalidDataException("Cuenta ya se encuentra verificada.");
        }

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
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

    private void sendVerificationEmail(User user) {
        String subject = "Verificación de cuenta";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #FF9D14; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">¡Bienvenido a Wander!</h2>"
                + "<p style=\"font-size: 16px;\">Por favor ingresa el siguiente código debajo para continuar:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Código de Verificación:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


}
