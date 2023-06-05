package org.oj.server.dao;

import org.oj.server.entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:26
 */
public interface MenuRepository extends MongoRepository<Menu, String> {
}
