package com.igrowker.wander.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsService {

    String uploadFile(String bucketName, String filePath, MultipartFile file);
}