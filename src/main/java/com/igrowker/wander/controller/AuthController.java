
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

@RestController
@RequestMapping("/api/autenticacion")
public class AuthController {

    @Autowired
    private AuthService authService;


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
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}

