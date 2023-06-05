package org.oj.server.dao;

import org.oj.server.entity.Faculty;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:25
 */
public interface FacultyRepository extends MongoRepository<Faculty, String> {
}
