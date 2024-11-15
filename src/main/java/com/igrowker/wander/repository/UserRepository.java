
package com.igrowker.wander.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.igrowker.wander.model.User;
import java.util.Optional;

/**
 *
 * @author AdolfoJF
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}