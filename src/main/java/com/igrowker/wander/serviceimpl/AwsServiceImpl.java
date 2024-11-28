package com.igrowker.wander.serviceimpl;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.igrowker.wander.config.AmazonS3Config;
import com.igrowker.wander.entity.AmazonImage;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.S3Repository;
import com.igrowker.wander.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AwsServiceImpl implements AwsService {

    @Autowired
    private AmazonS3Config amazonS3Config;;

    @Autowired
    private S3Repository amazonImageRepository;

    public List<AmazonImage> insertImages(List<MultipartFile> images) {
        int maxImages = 5;
        if (images.size() <= maxImages) {
            List<AmazonImage> amazonImages = new ArrayList<>();
            images.forEach(image -> amazonImages.add(uploadImageToAmazon(image)));
            return amazonImages;
        }
        return null;
    }

    public AmazonImage uploadImageToAmazon(MultipartFile image) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.isEnabled()) {
            // Upload file to Amazon.
            String url = uploadMultipartFile(image);
            // Save image information on MongoDB and return them.
            AmazonImage amazonImage = new AmazonImage();
            amazonImage.setImageUrl(url);
            return amazonImageRepository.insert(amazonImage);
        }
        return null;
    }

    private String uploadMultipartFile(MultipartFile multipartFile) {
        String fileUrl;
        try {
            // Get the file from MultipartFile
            File file = convertMultipartToFile(multipartFile);
            // Extract the file name
            String fileName = generateFileName(multipartFile);
            uploadPublicFile(fileName, file);
            file.delete();
            fileUrl = amazonS3Config.getUrl().concat(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error durante la conversión de archivo.", e);
        }
        return fileUrl;
    }

    private void uploadPublicFile(String fileName, File file) {
        amazonS3Config.getClient().putObject(new PutObjectRequest(amazonS3Config.getBucketName(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public File convertMultipartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return convertedFile;
    }

    public String generateFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multipartFile.getOriginalFilename()).replace(" ", "_");
    }
}