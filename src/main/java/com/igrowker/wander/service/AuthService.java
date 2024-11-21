package com.igrowker.wander.service;

import com.igrowker.wander.dto.user.*;

public interface AuthService {

    ResponseUserDto registerUser(RegisterUserDto userDto);

    ResponseUserDto verifyUser(RequestVerifyUserDto verifyUserDto);

    LoginResponse authenticateUser(LoginRequest loginRequest);

    String logout(String authorizationHeader);

}
