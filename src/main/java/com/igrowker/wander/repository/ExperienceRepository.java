package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ExperienceEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {
    @Query("{ 'location.1': ?0, 'price': { $lte: ?1 }, 'title': { $regex: ?2, $options: 'i' } }")
    List<ExperienceEntity> findByCityAndPriceLessThanEqualAndTitleContaining(String city, Double maxPrice, String title);

    @Query("{ 'location.1': ?0, 'price': { $lte: ?1 } }")
    List<ExperienceEntity> findByCityAndPriceLessThanEqual(String city, Double maxPrice);

    @Query("{ 'location.1': ?0, 'title': { $regex: ?1, $options: 'i' } }")
    List<ExperienceEntity> findByCityAndTitleContaining(String city, String title);

    @Query("{ 'location.1': ?0 }")
    List<ExperienceEntity> findByCity(String city);

    @Query("{ 'location.0': ?0, 'price': { $lte: ?1 }, 'title': { $regex: ?2, $options: 'i' } }")
    List<ExperienceEntity> findByCountryAndPriceLessThanEqualAndTitleContaining(String country, Double maxPrice, String title);

    @Query("{ 'location.0': ?0, 'price': { $lte: ?1 } }")
    List<ExperienceEntity> findByCountryAndPriceLessThanEqual(String country, Double maxPrice);

    @Query("{ 'location.0': ?0, 'title': { $regex: ?1, $options: 'i' } }")
    List<ExperienceEntity> findByCountryAndTitleContaining(String country, String title);

    @Query("{ 'location.0': ?0 }")
    List<ExperienceEntity> findByCountry(String country);

    List<ExperienceEntity> findByLocationContainsAndPriceLessThanEqualAndTitleContaining(List<String> location, Double maxPrice, String title);

    List<ExperienceEntity> findByLocationContainsAndPriceLessThanEqual(List<String> location, Double maxPrice);

    List<ExperienceEntity> findByLocationContainsAndTitleContaining(List<String> location, String title);

    List<ExperienceEntity> findByLocationContains(List<String> location);

    List<ExperienceEntity> findByPriceLessThanEqualAndTitleContaining(Double maxPrice, String title);

    List<ExperienceEntity> findByTitleContaining(String title);

    List<ExperienceEntity> findByPriceLessThanEqual(Double maxPrice);

    List<ExperienceEntity> findByTagsContaining(String tag);

    List<ExperienceEntity> findByTagsIn(List<String> tags);

    List<ExperienceEntity> findAll();

    List<ExperienceEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<ExperienceEntity> findAllByOrderByRatingDesc(Pageable pageable);
}
