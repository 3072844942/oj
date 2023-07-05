package org.oj.server.dao;

import org.oj.server.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:01
 */
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByNumber(String number);
}
