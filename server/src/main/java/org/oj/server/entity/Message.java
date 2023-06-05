package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 留言
 *
 * @author bin
 * @date 2021/08/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("message")
public class Message {
    /**
     * 主键id
     */
    @Id
    private String id;

    /**
     * 发布人id
     */
    private String userId;

    /**
     * 接受人id
     */
    private String toUserId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 是否已读
     */
    private EntityStateEnum state;

    /**
     * 创建时间
     */
    private Long createTime;
}
