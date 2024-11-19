package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.LoginResponse;
import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.exception.InvalidUserCredentialsException;
import com.igrowker.wander.exception.ResourceAlreadyExistsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.AuthService;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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

}
