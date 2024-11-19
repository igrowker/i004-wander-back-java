package com.igrowker.wander.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.igrowker.wander.repository.RevokedTokenRepository;
import com.igrowker.wander.entity.RevokedToken;

import java.util.Date;

@Service
public class LogoutService {

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    public void invalidateToken(String token, Date expirationDate) {
        if (!revokedTokenRepository.existsByToken(token)) {
            revokedTokenRepository.save(new RevokedToken(token, expirationDate));
        }
    }
}
