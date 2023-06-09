package org.oj.server.dao;

import org.oj.server.entity.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午2:59
 */
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByUrl(String url);

    boolean existsByUrl(String url);
}
