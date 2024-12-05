package com.igrowker.wander.controller;

import com.igrowker.wander.dto.user.RequestUpdateUserDto;
import com.igrowker.wander.dto.user.UserDto;
import com.igrowker.wander.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * get authenticated user profile
     *
     * @return UserDto with user profile data
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile() {
        UserDto userDto = userService.getUserProfile(null);
        return ResponseEntity.ok(userDto);
    }

    /**
     * get user profile by ID
     *
     * @return UserDto with user profile data
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserDto> getUserProfileById(@PathVariable String id) {
        UserDto userDto = userService.getUserProfile(id);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Update the authenticated user's profile
     * @param userUpdates user data to update
     * @return dto with updated user profile data
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(@Valid
            @RequestBody RequestUpdateUserDto userUpdates) {
        UserDto updatedUser = userService.updateUserProfile(userUpdates);
        return ResponseEntity.ok(updatedUser);
    }


}
