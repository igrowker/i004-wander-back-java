package com.igrowker.wander.controller;

import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.dto.LoginResponse;
import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autenticacion")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> registerUser(@RequestBody RegisterUserDto userDto) {
        ResponseUserDto registeredUser = authService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /**
     * Authenticates the user and returns a JWT token if the credentials are valid
     *
     * @param loginRequest login request containing email and pwd
     * @return ResponseEntity containing the authentication token if successful,
     * *         or an HTTP 401 Unauthorized status if fails
     */
    @Operation(
            summary = "User login",
            description = "This endpoint allows a user to login by providing their credentials. If successful, a JWT token is returned."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String response = authService.logout(authorizationHeader);
        return ResponseEntity.ok(response);
    }
}

