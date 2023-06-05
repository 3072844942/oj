package org.oj.server.dao;

import org.oj.server.entity.UserAuth;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 下午3:01
 */
public interface UserAuthRepository extends MongoRepository<UserAuth, String> {
}
