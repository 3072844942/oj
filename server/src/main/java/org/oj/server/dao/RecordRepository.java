package org.oj.server.dao;

import org.oj.server.entity.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:29
 */
public interface RecordRepository extends MongoRepository<Record, String> {
}
