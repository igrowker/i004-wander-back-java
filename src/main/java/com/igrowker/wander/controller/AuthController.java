
package com.igrowker.wander.controller;



import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.igrowker.wander.service.AuthService;
import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.dto.AuthResponse;
import com.igrowker.wander.exception.AuthenticationException;
import com.igrowker.wander.service.LogoutService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/autenticacion")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private LogoutService logoutService;
    
    @Value("${jwt.secret}")
    private String secretKey;

<<<<<<< HEAD
    @PostMapping("/users/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Validate the email format before proceeding
            if (!isValidEmail(loginRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid email
            }

            // Attempt to authenticate the user
            String token = authService.authenticateUser(loginRequest);
=======

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
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest);
        if (token != null) {
>>>>>>> fe1faf3485ca6bed9a22b7777ae76f677c25d678
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException ex) {
            // Handle authentication exception (invalid email or password)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    // Auxiliary method to validate the email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; // Regular expression for email
        return email != null && email.matches(emailRegex);
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

