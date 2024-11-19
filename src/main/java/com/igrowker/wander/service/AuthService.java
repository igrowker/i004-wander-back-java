package com.igrowker.wander.service;

import com.igrowker.wander.dto.LoginRequest;
import com.igrowker.wander.dto.LoginResponse;
import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;

public interface AuthService {

    ResponseUserDto registerUser(RegisterUserDto userDto);

    LoginResponse authenticateUser(LoginRequest loginRequest);

}
