package com.igrowker.wander.service;

import com.igrowker.wander.dto.user.*;

public interface AuthService {

    ResponseUserDto registerUser(RegisterUserDto userDto);

    ResponseUserDto verifyUser(RequestVerifyUserDto verifyUserDto);

    LoginResponse authenticateUser(LoginRequest loginRequest);

    String logout(String authorizationHeader);
    
    void sendForgotPasswordEmail(String email);

    void resetPassword(String mail, String code, String newPassword);
    
    void resendVerificationCode(String email);

}
