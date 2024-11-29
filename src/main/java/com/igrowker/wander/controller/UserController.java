package com.igrowker.wander.controller;

import com.igrowker.wander.dto.user.RequestUpdateUserDto;
import com.igrowker.wander.dto.user.UserDto;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

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
