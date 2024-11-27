package com.igrowker.wander.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadAvatarService {

    boolean setAvatar(String bucketName, String filePath, MultipartFile imageFile);
}
