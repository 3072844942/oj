package org.oj.server.dao;

import org.oj.server.entity.Article;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 上午11:20
 */
public interface ArticleRepository extends MongoRepository<Article, String> {
}
