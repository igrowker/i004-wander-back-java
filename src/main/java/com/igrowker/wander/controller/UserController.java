package com.igrowker.wander.controller;

import com.igrowker.wander.dto.user.RequestUpdateUserDto;
import com.igrowker.wander.dto.user.UserDto;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * get user profile
     *
     * @return UserDto with user profile data
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile() {
        UserDto userDto = userService.getUserProfile();
        return ResponseEntity.ok(userDto);
    }

    /**
     * Update the authenticated user's profile
     * @param userUpdates user data to update
     * @return dto with updated user profile data
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(
            @RequestBody RequestUpdateUserDto userUpdates) {
        UserDto updatedUser = userService.updateUserProfile(userUpdates);
        return ResponseEntity.ok(updatedUser);
    }


}
