package org.oj.server.dao;

import org.oj.server.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:20
 */
public interface CategoryRepository extends MongoRepository<Category, String> {
}
