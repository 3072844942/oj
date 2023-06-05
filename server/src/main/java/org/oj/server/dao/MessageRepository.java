package org.oj.server.dao;

import org.oj.server.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:27
 */
public interface MessageRepository extends MongoRepository<Message, String> {
}
