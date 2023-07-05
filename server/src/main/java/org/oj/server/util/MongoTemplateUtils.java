package org.oj.server.util;

import org.oj.server.constant.MongoConst;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.PermissionUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author march
 * @since 2023/7/5 上午9:27
 */
@Service
public class MongoTemplateUtils {
    private final MongoTemplate mongoTemplate;

    public MongoTemplateUtils(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void delete(String id, Class<?> c) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoConst.ID).is(id));

        Update update = new Update();
        update.set(MongoConst.STATE, false);

        mongoTemplate.updateFirst(query, update, c);
    }

    public void delete(String id, Class<?> c, String authorId) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite(authorId)) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoConst.ID).is(id));

        Update update = new Update();
        update.set(MongoConst.STATE, false);

        mongoTemplate.updateFirst(query, update, c);
    }

    public void delete(List<String> ids, Class<?> c) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(MongoConst.ID).in(ids));

        Update update = new Update();
        update.set(MongoConst.STATE, false);

        mongoTemplate.updateFirst(query, update, c);
    }
}
