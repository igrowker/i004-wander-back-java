package com.igrowker.wander.service;

import com.igrowker.wander.dto.user.RequestUpdateUserDto;
import com.igrowker.wander.dto.user.UserDto;
import com.igrowker.wander.entity.User;

public interface UserService {
    UserDto getUserProfile(String id);
    UserDto updateUserProfile(RequestUpdateUserDto userUpdates);

}
