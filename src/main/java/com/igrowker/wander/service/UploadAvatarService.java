package com.igrowker.wander.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadAvatarService {

    boolean setAvatar(MultipartFile imageFile);
}
