package com.igrowker.wander.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.igrowker.wander.entity.ExperienceEntity;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {

    // Método para buscar por ubicación, precio máximo y título
    List<ExperienceEntity> findByLocationAndPriceLessThanEqualAndTitleContaining(String location, Double maxPrice, String title);
    
    List<ExperienceEntity> findByLocationAndPriceLessThanEqual(String location, Double maxPrice);

    // Método para buscar por ubicación y título
    List<ExperienceEntity> findByLocationAndTitleContaining(String location, String title);

    // Método para buscar por precio máximo y título
    List<ExperienceEntity> findByPriceLessThanEqualAndTitleContaining(Double maxPrice, String title);

    // Método para buscar solo por título
    List<ExperienceEntity> findByTitleContaining(String title);

    // Método para buscar solo por ubicación
    List<ExperienceEntity> findByLocation(String location);

    // Método para buscar solo por precio máximo
    List<ExperienceEntity> findByPriceLessThanEqual(Double maxPrice);

    // Método para obtener todas las experiencias
    List<ExperienceEntity> findAll();
}
