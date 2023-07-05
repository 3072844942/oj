package org.oj.server.dto;

import lombok.*;
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
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    /**
     * 主键id
     */
    private String id;

    /**
     * 发布人id
     */
    private String userId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Long createTime;

    private Long updateTime;
}
