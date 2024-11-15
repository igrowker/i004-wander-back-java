package com.igrowker.wander.repository;

import com.igrowker.wander.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {
    boolean existsByEmail(String email);
}
