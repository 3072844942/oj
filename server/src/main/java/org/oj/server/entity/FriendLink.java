package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.dto.FriendLinkDTO;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
@Document(MongoConst.FRIEND_link)
public class FriendLink {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 链接名
     */
    private String title;

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
    private String content;

    /**
     * 创建时间
     */
    @CreatedDate
    private Long createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private Long updateTime;

    public static FriendLink of(FriendLinkDTO friendLinkDTO) {
        return BeanCopyUtils.copyObject(friendLinkDTO, FriendLink.class);
    }
}
