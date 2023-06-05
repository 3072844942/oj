package org.oj.server.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 友链列表
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Getter
@AllArgsConstructor
public class FriendLinkDTO {

    /**
     * id
     */
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
}
