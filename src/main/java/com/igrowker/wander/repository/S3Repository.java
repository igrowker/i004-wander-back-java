package com.igrowker.wander.repository;

import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public interface S3Repository {

    String uploadFile(String bucketName, String fileName, File fileObj);
}
