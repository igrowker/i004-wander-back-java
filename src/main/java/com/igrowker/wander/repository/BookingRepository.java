package com.igrowker.wander.repository;

import com.igrowker.wander.dto.experience.ExperienceReservationCountDto;
import com.igrowker.wander.entity.BookingEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Instant;
import java.util.List;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface BookingRepository extends MongoRepository<BookingEntity, String> {

    List<BookingEntity> findByUserId(String userId);

    List<BookingEntity> findByExperienceId(String experienceId);

    List<BookingEntity> findByExperienceIdAndBookingDate(String experienceId, Instant bookingDate);

    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$experienceId', 'count': { '$sum': 1 } } }",
            "{ '$sort': { 'count': -1 } }",
            "{ '$limit': ?0 }"
    })
    List<ExperienceReservationCountDto> findTopReservedExperiences(int limit);
}

