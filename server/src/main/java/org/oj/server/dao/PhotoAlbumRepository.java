package org.oj.server.dao;

import org.oj.server.entity.PhotoAlbum;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author march
 * @since 2023/5/31 上午11:28
 */
public interface PhotoAlbumRepository extends MongoRepository<PhotoAlbum, String> {
}
