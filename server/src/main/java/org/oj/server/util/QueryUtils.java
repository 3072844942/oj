package org.oj.server.util;

import org.oj.server.constant.MongoConst;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Arrays;

/**
 * @author march
 * @since 2023/7/5 上午8:32
 */
public class QueryUtils {

    /**
     * 默认查询， 匹配读权限和作者
     * @param conditionDTO 查询条件
     * @return 默认查询
     */
    public static Query defaultQuery(ConditionDTO conditionDTO) {
        // 查询条件
        Query query = new Query();
        // 有读写权限
        if (PermissionUtil.enableRead(EntityStateEnum.DRAFT, "")) {
            // 随意读
            query.addCriteria(Criteria.where(MongoConst.STATE).is(EntityStateEnum.valueOf(conditionDTO.getState())));
        } else {
            EntityStateEnum state = EntityStateEnum.valueOf(conditionDTO.getState());
            // 如果读的不是公开
            if (!state.equals(EntityStateEnum.PUBLIC)) {
                throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
            } else {
                query.addCriteria(Criteria.where(MongoConst.STATE).is(state));
            }
        }
        // 指定了作者
        if (conditionDTO.getId() != null) {
            query.addCriteria(Criteria.where(MongoConst.USER_ID).is(conditionDTO.getId()));
        }
        return query;
    }

    public static void skip(Query query, ConditionDTO conditionDTO) {
        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
    }

    /**
     * 返回默认排序
     * @return 置顶降序， 更新时间降序
     */
    public static Sort defaultSort() {
        return Sort.by(Sort.Order.desc(MongoConst.IS_TOP),
                Sort.Order.desc(MongoConst.UPDATE_TIME));
    }

    public static void regexKeywords(Query query, String keywords, String... key) {
        if (keywords != null) {
            query.addCriteria(new Criteria().orOperator(
                    Arrays.stream(key).map(k -> Criteria.where(k).regex(keywords)).toList()
            ));
        }
    }
}
