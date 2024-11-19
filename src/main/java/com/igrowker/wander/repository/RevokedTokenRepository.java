package com.igrowker.wander.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.igrowker.wander.entity.RevokedToken;

@Repository
public interface RevokedTokenRepository extends MongoRepository<RevokedToken, String> {
    boolean existsByToken(String token);
}
