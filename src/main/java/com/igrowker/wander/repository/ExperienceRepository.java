package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ExperienceEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {

	List<ExperienceEntity> findByLocationContainsAndPriceLessThanEqualAndTitleContaining(List<String> location, Double maxPrice, String title);

    List<ExperienceEntity> findByLocationContainsAndPriceLessThanEqual(List<String> location, Double maxPrice);

    List<ExperienceEntity> findByLocationContainsAndTitleContaining(List<String> location, String title);

    List<ExperienceEntity> findByPriceLessThanEqualAndTitleContaining(Double maxPrice, String title);

    List<ExperienceEntity> findByTitleContaining(String title);

    List<ExperienceEntity> findByLocationContains(List<String> location);

    List<ExperienceEntity> findByPriceLessThanEqual(Double maxPrice);

    List<ExperienceEntity> findAll();

    List<ExperienceEntity> findByTagsContaining(String tag);

    List<ExperienceEntity> findByTagsIn(List<String> tags);
}
