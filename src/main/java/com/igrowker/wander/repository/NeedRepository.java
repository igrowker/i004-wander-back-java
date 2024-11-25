package com.igrowker.wander.repository;

import com.igrowker.wander.entity.Need;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeedRepository extends MongoRepository<Need, String> {
    List<Need> findByExperienceId(String experienceId);
}
