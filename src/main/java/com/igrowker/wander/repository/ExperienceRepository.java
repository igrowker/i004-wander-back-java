package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ExperienceEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {


    List<ExperienceEntity> findByLocation(String location);

    List<ExperienceEntity> findByPriceLessThanEqual(Double maxPrice);
 
    List<ExperienceEntity> findByLocationAndPriceLessThanEqual(String location, Double maxPrice);
}