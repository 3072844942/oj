package org.oj.server.dao;

import org.oj.server.entity.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:28
 */
public interface PhotoRepository extends MongoRepository<Photo, String> {
}
