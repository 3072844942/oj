package org.oj.server.dao;

import org.oj.server.entity.Contest;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:24
 */
public interface ContestRepository extends MongoRepository<Contest, String> {
}
