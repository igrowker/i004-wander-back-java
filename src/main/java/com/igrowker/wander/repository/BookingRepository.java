package com.igrowker.wander.repository;

import com.igrowker.wander.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findByUserId(String userId);
    List<Booking> findByExperienceId(String experienceId);
}
