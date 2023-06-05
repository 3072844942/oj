package org.oj.server.dto;

import lombok.*;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 公告
 *
 * @author march
 * @since 2023/5/31 上午10:27
 */
@Getter
@AllArgsConstructor
public class NoticeDTO {

    /**
     * id
     */
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
    private Integer status;
}
