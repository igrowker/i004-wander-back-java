package com.igrowker.wander.controller;

import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> registerUser(@RequestBody RegisterUserDto userDto) {

        ResponseUserDto registeredUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);

    }

    /**
     * Update the authenticated user's profile
     * @param userUpdates user data to update
     * @return updated user profile
     */
    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(
            @RequestBody User userUpdates) {
        try {
            User updatedUser = userService.updateUserProfile(userUpdates);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
