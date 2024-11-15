package com.igrowker.wander.service;


import com.igrowker.wander.dto.UserDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User registerUser(UserDto userDto) {
        // Validar si el email ya existe
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setId(generateId()); // Implementar método para generar ID
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setPreferences(userDto.getPreferences());
        user.setLocation(userDto.getLocation());
        user.setCreatedAt(LocalDateTime.now());
        user.setBookings(new ArrayList<>());

        return userRepository.save(user);
    }

    private Long generateId() {
        // Implementación simple para generar ID
        return System.currentTimeMillis();
    }
}