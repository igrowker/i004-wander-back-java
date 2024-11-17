package com.igrowker.wander.controller;

import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

}
