package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.ResourceAlreadyExistsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * Updates a user's profile in the database.
     *
     * @param userUpdates a UserEntity object containing the new data to update
     * @return the updated UserEntity object with the new data
     * @throws RuntimeException if no user is found with the provided email
     */
    @Override
    public User updateUserProfile(User userUpdates) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("El usuario no está autenticado.");
        }
        User authenticatedUser = (User) authentication.getPrincipal();

        return userRepository.findByEmail(authenticatedUser.getEmail())
                .map(existingUser -> {
                    //Solo actualiza los campos no nulos y los que no estan vacios
                    if (userUpdates.getName() != null && !userUpdates.getName().trim().isEmpty()) {
                        existingUser.setName(userUpdates.getName());
                    }
                    if (userUpdates.getEmail() != null && !userUpdates.getEmail().trim().isEmpty()) {
                        existingUser.setEmail(userUpdates.getEmail());
                    }
                    if (userUpdates.getLocation() != null && !userUpdates.getLocation().isEmpty()) {
                        existingUser.setLocation(userUpdates.getLocation());
                    }
                    if (userUpdates.getRole() != null && !userUpdates.getRole().isEmpty()) {
                        existingUser.setRole(userUpdates.getRole());
                    }
                    if (userUpdates.getPreferences() != null && !userUpdates.getPreferences().isEmpty()) {
                        existingUser.setPreferences(userUpdates.getPreferences());
                    }

                    if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
                    }


                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + authenticatedUser.getEmail()));
    }

}