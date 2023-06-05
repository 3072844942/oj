package org.oj.server.dao;

import org.oj.server.entity.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 下午3:00
 */
public interface TagRepository extends MongoRepository<Tag, String> {
}
