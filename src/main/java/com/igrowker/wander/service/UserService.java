package com.igrowker.wander.service;

import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;

public interface UserService {

    ResponseUserDto registerUser(RegisterUserDto userDto);
}
