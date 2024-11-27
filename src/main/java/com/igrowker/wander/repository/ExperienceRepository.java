package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ExperienceEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {

    List<ExperienceEntity> findByLocationAndPriceLessThanEqualAndTitleContaining(String location, Double maxPrice, String title);
    
    List<ExperienceEntity> findByLocationAndPriceLessThanEqual(String location, Double maxPrice);

    List<ExperienceEntity> findByLocationAndTitleContaining(String location, String title);

    List<ExperienceEntity> findByPriceLessThanEqualAndTitleContaining(Double maxPrice, String title);

    List<ExperienceEntity> findByTitleContaining(String title);

    List<ExperienceEntity> findByLocation(String location);

    List<ExperienceEntity> findByPriceLessThanEqual(Double maxPrice);

    List<ExperienceEntity> findAll();

    List<ExperienceEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<ExperienceEntity> findAllByOrderByRatingDesc(Pageable pageable);
}
