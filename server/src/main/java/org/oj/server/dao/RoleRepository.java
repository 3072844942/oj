package org.oj.server.dao;

import org.oj.server.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 下午2:59
 */
public interface RoleRepository extends MongoRepository<Role, String> {
}
