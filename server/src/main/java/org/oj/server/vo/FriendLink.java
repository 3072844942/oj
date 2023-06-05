package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 友链列表
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("friend_link")
public class FriendLink {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 链接名
     */
    private String name;

    /**
     * 链接头像
     */
    private String avatar;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 介绍
     */
    private String desc;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;
}
