package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ReviewEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ReviewRepository extends MongoRepository<ReviewEntity, String>{
	// Method to find reviews by experience ID
    List<ReviewEntity> findByExperienceId(String experienceId);
}
