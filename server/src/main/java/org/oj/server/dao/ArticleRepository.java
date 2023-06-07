package org.oj.server.dao;

import org.oj.server.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:20
 */
public interface ArticleRepository extends MongoRepository<Article, String> {
}
