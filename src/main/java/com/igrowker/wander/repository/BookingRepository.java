package com.igrowker.wander.repository;

import com.igrowker.wander.entity.BookingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<BookingEntity, String> {

    List<BookingEntity> findByUserId(String userId);
    List<BookingEntity> findByExperienceId(String experienceId);

}