package org.oj.server.dao;

import org.oj.server.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 下午3:01
 */
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {
}
