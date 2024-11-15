package com.igrowker.wander.service;


import com.igrowker.wander.dto.UserDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        User user = new User();
        user.setId(generateId());
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
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? 1 :
                users.stream().max(Comparator.comparing(User::getId)).get().getId() + 1;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}