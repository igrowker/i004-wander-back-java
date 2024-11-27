package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.repository.S3Repository;
import com.igrowker.wander.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class AwsServiceImpl implements AwsService {


    private S3Repository s3Repository;

    @Autowired
    public AwsServiceImpl(S3Repository s3Repository) {

        this.s3Repository = s3Repository;
    }

    @Override
    public String uploadFile(String bucketName, String filePath, MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        return s3Repository.uploadFile(bucketName, filePath + fileName, fileObj);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.err.println("Error converting multipartFile to file " + e.getMessage());
        }
        return convertedFile;
    }
}