package com.igrowker.wander.repository;

import com.igrowker.wander.entity.BookingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface BookingRepository extends MongoRepository<BookingEntity, String> {

    List<BookingEntity> findByUserId(String userId);

    List<BookingEntity> findByExperienceId(String experienceId);

}

