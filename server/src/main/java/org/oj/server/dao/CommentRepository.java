package org.oj.server.dao;

import org.oj.server.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
/**
 * @author march
 * @since 2023/5/31 上午11:21
 */
public interface CommentRepository extends MongoRepository<Comment, String> {
}
