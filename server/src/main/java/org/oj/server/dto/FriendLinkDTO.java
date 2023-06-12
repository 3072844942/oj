package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.FriendLink;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.StringUtils;

/**
 * 友链列表
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "友链信息")
public class FriendLinkDTO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 链接名
     */
    @Schema(description = "链接名")
    private String name;

    /**
     * 链接头像
     */
    @Schema(description = "链接头像")
    private String avatar;

    /**
     * 链接地址
     */
    @Schema(description = "链接地址")
    private String url;

    /**
     * 介绍
     */
    @Schema(description = "介绍")
    private String desc;

    public static void check(FriendLinkDTO friendLinkDTO) {
        if (!StringUtils.isSpecifiedLength(friendLinkDTO.getName(), 0, 20)) {
            throw new WarnException("名称长度超限");
        }
        if (!StringUtils.isSpecifiedLength(friendLinkDTO.getDesc(), 0, 1000)) {
            throw new WarnException("描述长度超限");
        }
    }

    public static FriendLinkDTO of(FriendLink friendLink) {
        return BeanCopyUtils.copyObject(friendLink, FriendLinkDTO.class);
    }
}
