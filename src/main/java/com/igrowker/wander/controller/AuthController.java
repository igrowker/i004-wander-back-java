
package com.igrowker.wander.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private LogoutService logoutService;
    
    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping("/users/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Validate the email format before proceeding
            if (!isValidEmail(loginRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid email
            }

            // Attempt to authenticate the user
            String token = authService.authenticateUser(loginRequest);
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

