package com.igrowker.wander.service;

import com.igrowker.wander.dto.user.LoginRequest;
import com.igrowker.wander.dto.user.LoginResponse;
import com.igrowker.wander.dto.user.RegisterUserDto;
import com.igrowker.wander.dto.user.ResponseUserDto;

public interface AuthService {

    ResponseUserDto registerUser(RegisterUserDto userDto);

    LoginResponse authenticateUser(LoginRequest loginRequest);

    String logout(String authorizationHeader);
    
    void sendForgotPasswordEmail(String email);

    void resetPassword(String token, String newPassword);

}
