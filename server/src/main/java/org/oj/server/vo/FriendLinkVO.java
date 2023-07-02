package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.FriendLink;
import org.oj.server.util.BeanCopyUtils;

/**
 * @author march
 * @since 2023/6/12 上午10:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "友链信息")
public class FriendLinkVO {
    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 链接名
     */
    @Schema(description = "名称")
    private String title;

    /**
     * 链接头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 链接地址
     */
    @Schema(description = "地址")
    private String url;

    /**
     * 介绍
     */
    @Schema(description = "介绍")
    private String content;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    /**
     * 修改时间
     */
    @Schema(description = "更新时间")
    private Long updateTime;

    public static FriendLinkVO of(FriendLink friendLink) {
        return BeanCopyUtils.copyObject(friendLink, FriendLinkVO.class);
    }
}
