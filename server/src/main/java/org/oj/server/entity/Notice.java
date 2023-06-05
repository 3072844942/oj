package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 公告
 *
 * @author march
 * @since 2023/5/31 上午10:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("notice")
public class Notice {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 作者
     */
    private String userId;

    /**
     * 文章缩略图
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 文章状态
     */
    private EntityStateEnum status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}
