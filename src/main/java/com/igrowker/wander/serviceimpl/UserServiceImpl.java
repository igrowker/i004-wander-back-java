package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.ResourceAlreadyExistsException;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseUserDto registerUser(RegisterUserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
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
}