package com.igrowker.wander.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.igrowker.wander.entity.ExperienceEntity;

public interface ExperienceRepository extends MongoRepository<ExperienceEntity, String> {

}
