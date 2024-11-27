package com.igrowker.wander.service;

import com.igrowker.wander.entity.User;

public interface EmailService {
    void sendVerificationEmail(User user);

    void sendPasswordResetEmail(User user);

}
