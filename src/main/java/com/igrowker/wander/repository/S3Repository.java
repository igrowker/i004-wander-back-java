package com.igrowker.wander.repository;

import com.igrowker.wander.entity.AmazonImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface S3Repository extends MongoRepository<AmazonImage, String> {
}
