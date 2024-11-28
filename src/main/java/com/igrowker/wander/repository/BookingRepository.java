package com.igrowker.wander.repository;

import com.igrowker.wander.dto.experience.ExperienceReservationCountDto;
import com.igrowker.wander.dto.experience.RequestExperienceDto;
import com.igrowker.wander.entity.BookingEntity;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.Map;

@EnableMongoRepositories(basePackages = "com.igrowker.wander.repository")
public interface BookingRepository extends MongoRepository<BookingEntity, String> {

    List<BookingEntity> findByUserId(String userId);

    List<BookingEntity> findByExperienceId(String experienceId);

    @Aggregation(pipeline = {
            "{ '$group': { '_id': '$experienceId', 'count': { '$sum': 1 } } }",
            "{ '$sort': { 'count': -1 } }",
            "{ '$limit': ?0 }"
    })
    List<ExperienceReservationCountDto> findTopReservedExperiences(int limit);
}

