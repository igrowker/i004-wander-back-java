package com.igrowker.wander.service;

import com.igrowker.wander.entity.AmazonImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AwsService {

    List<AmazonImage> insertImages(List<MultipartFile> images);

    AmazonImage uploadImageToAmazon(MultipartFile multipartFile);
}