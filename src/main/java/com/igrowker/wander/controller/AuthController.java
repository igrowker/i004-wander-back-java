package com.igrowker.wander.controller;

import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.dto.LoginResponse;
import com.igrowker.wander.service.LogoutService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/autenticacion")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private LogoutService logoutService;

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
     *  *         or an HTTP 401 Unauthorized status if fails
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
        // Verificar que el header Authorization existe y sigue el formato correcto
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // Extraer el token del header
                String token = authorizationHeader.substring(7);

                // Parsear el token para obtener las claims
                Claims claims = Jwts.parser()
                                    .setSigningKey(secretKey)
                                    .parseClaimsJws(token)
                                    .getBody();

                // Invalidar el token guardándolo en la lista de revocación
                logoutService.invalidateToken(token, claims.getExpiration());

                // Respuesta de éxito
                return ResponseEntity.ok("Token invalidado correctamente.");
            } catch (Exception e) {
                // Manejar errores relacionados con el token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Error al procesar el token: " + e.getMessage());
            }
        }
        // Respuesta si el header no está presente o no sigue el formato esperado
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El header Authorization no es válido.");
    }
}

