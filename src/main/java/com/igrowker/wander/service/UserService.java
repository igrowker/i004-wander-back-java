package com.igrowker.wander.service;

import com.igrowker.wander.dto.RegisterUserDto;
import com.igrowker.wander.dto.ResponseUserDto;
import com.igrowker.wander.entity.User;

public interface UserService {

    ResponseUserDto registerUser(RegisterUserDto userDto);

    User updateUserProfile(User userUpdates);
}
