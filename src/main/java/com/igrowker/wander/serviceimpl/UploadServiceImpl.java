package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UserRepository userRepository;

    public void setAvatar(String imageUrl, String userId) {
        Optional<User> userById = userRepository.findById(userId);
        User userEntity = userById.get();
        userEntity.setAvatar(imageUrl);
        userRepository.save(userEntity);
        }
}