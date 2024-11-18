package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ExperienceEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {
	
    // Method to find experiences by location
    List<ExperienceEntity> findByLocation(String location);

    // Method to find experiences by type
    List<ExperienceEntity> findByType(String type);

    // Method to find experiences with a maximum price
    List<ExperienceEntity> findByPriceLessThanEqual(Double maxPrice);

    // Method to find experiences by location and type
    List<ExperienceEntity> findByLocationAndType(String location, String type);

    // Method to find experiences by location, type, and maximum price
    List<ExperienceEntity> findByLocationAndTypeAndPriceLessThanEqual(String location, String type, Double maxPrice);

}

