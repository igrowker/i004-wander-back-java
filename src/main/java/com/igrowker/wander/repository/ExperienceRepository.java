package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ExperienceEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {

	// Method to search experiences by location
    List<ExperienceEntity> findByLocation(String location);

    // Method to search experiences with a maximum price
    List<ExperienceEntity> findByPriceLessThanEqual(Double maxPrice);
 
    // Method to search experiences by location and maximum price
    List<ExperienceEntity> findByLocationAndPriceLessThanEqual(String location, Double maxPrice);
}

