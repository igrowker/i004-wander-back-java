package com.igrowker.wander.controller;

import com.igrowker.wander.dto.user.*;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import java.util.Map;
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
    public ResponseEntity<ResponseUserDto> registerUser(@com.igrowker.wander.controller.Valid @RequestBody RegisterUserDto userDto) {
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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify-user")
    public ResponseEntity<ResponseUserDto> verifyUser(@Valid @RequestBody RequestVerifyUserDto requestVerifyUserDto) {
        ResponseUserDto verifiedUser = authService.verifyUser(requestVerifyUserDto);
        return ResponseEntity.ok(verifiedUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String response = authService.logout(authorizationHeader);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        authService.sendForgotPasswordEmail(request.get("email"));
        return ResponseEntity.ok("Correo enviado si el email existe en nuestro sistema");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        authService.resetPassword(request.get("email"),request.get("code"), request.get("newPassword"));
        return ResponseEntity.ok("Contraseña restablecida con éxito");
    }
    
    @PostMapping("/reenviar-codigo")
    public ResponseEntity<String> resendVerificationCode(@RequestBody Map<String, String> request) {
        authService.resendVerificationCode(request.get("email"));
        return ResponseEntity.ok("Nuevo código de verificación enviado al correo proporcionado.");
    }

}

