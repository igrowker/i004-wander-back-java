
package com.igrowker.wander.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.model.User;
import com.igrowker.wander.repository.UserRepository;

/**
 *
 * @author AdolfoJF
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String authenticateUser(LoginRequest loginRequest) {
        // Buscar el usuario en la base de datos por nombre de usuario
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            // Generar token de autenticación (JWT)
            String token = generateToken(user);
            return token;
        }

        // Si la autenticación falla, retorna null
        return null;
    }

    private String generateToken(User user) {
        // Implementación de generación de token JWT
        return "tokenGenerado"; // Reemplaza esto con la lógica real
    }
}
