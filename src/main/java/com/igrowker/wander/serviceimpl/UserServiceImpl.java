package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.user.RequestUpdateUserDto;
import com.igrowker.wander.dto.user.UserDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidDataException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("El usuario no está autenticado");
        }

        User authenticatedUser = (User) authentication.getPrincipal();
        User existingUser = userRepository.findByEmail(authenticatedUser.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró usuario con email: " + authenticatedUser.getEmail()));

        return convertToUserDto(existingUser);
    }

    /**
     * Updates a user's profile in the database.
     *
     * @param userUpdates a RequestUpdateUserDto object containing the new data to update
     * @return a dto object with the user new data
     * @throws RuntimeException if no user is found with the provided email
     */
    @Override
    public UserDto updateUserProfile(RequestUpdateUserDto userUpdates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("El usuario no está autenticado");
        }

        User authenticatedUser = (User) authentication.getPrincipal();

        User existingUser = userRepository.findByEmail(authenticatedUser.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + authenticatedUser.getEmail()));

        if (userUpdates.getName() != null && userUpdates.getName().isBlank()) {
            throw new InvalidDataException("El nombre no puede estar vacío");
        }
        if (userUpdates.getPassword() != null && userUpdates.getPassword().length() < 8) {
            throw new InvalidDataException("La contraseña debe tener al menos 8 caracteres");
        }

        if (userUpdates.getName() != null){
            existingUser.setName(userUpdates.getName());
        }
        if (userUpdates.getEmail() != null){
            existingUser.setEmail(userUpdates.getEmail());
        }
        if (userUpdates.getLocation() != null){
            existingUser.setLocation(userUpdates.getLocation());
        }
        if (userUpdates.getRole() != null){
            existingUser.setRole(userUpdates.getRole());
        }
        if (userUpdates.getPreferences() != null){
            existingUser.setPreferences(userUpdates.getPreferences());
        }
        if (userUpdates.getPassword() != null){
            existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
        }
        if (userUpdates.getPhone() != null){
            existingUser.setPhone(userUpdates.getPhone());
        }

        User updatedUser = userRepository.save(existingUser);
        return convertToUserDto(updatedUser);
    }


    private UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .preferences(user.getPreferences())
                .location(user.getLocation())
                .phone(user.getPhone())
                .build();
    }

}