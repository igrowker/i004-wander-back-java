package com.igrowker.wander.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Repository
public class S3RepositoryImpl implements S3Repository{

    @Autowired
    private S3Client s3Client;

    public String uploadFile(String bucketName, String fileName, File fileObj) {
        s3Client.putObject(request ->
                                request
                                .bucket(bucketName)
                                .key(file.getName())
                                .metadata(metadata)
                                .ifNoneMatch("*"),
                file.toPath());
        fileObj.delete();
        return "File uploaded : " + fileName;
    }
}
