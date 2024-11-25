package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.security.JwtService;
import com.igrowker.wander.service.UploadAvatarService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UploadAvatarServiceImpl implements UploadAvatarService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public boolean setAvatar(MultipartFile imageUrl) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getUsername());
        //Optional<User> user = userRepository.findByEmail(email);
        try {
           if (user.isEnabled()) {
                /*User userEntity = user.get();
                userEntity.setAvatar(imageUrl);
                userRepository.save(userEntity);
                */
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
