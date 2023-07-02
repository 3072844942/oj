package org.oj.server.dao;

import org.oj.server.entity.OperationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author march
 * @since 2023/7/2 下午3:45
 */
@Repository
public interface OperationLogRepository extends MongoRepository<OperationLog, String> {
}
