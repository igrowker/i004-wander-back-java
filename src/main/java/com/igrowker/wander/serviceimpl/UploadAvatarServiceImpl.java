package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.UploadAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UploadAvatarServiceImpl implements UploadAvatarService {

    @Autowired
    private UserRepository userRepository;

    public boolean setAvatar(String email, String imageUrl){
        Optional<User> userActual = userRepository.findByEmail(email);
        if (userActual.isPresent()) {
            User user = userActual.get();
            user.setAvatar(imageUrl);
            userRepository.save(user);
            return true;
        }
        else return false;
    }
}
