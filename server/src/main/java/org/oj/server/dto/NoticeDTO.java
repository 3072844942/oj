package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oj.server.entity.Notice;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.StringUtils;
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
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "公告信息")
public class NoticeDTO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 作者
     */
    @Schema(description = "作者id")
    private String userId;

    /**
     * 文章缩略图
     */
    @Schema(description = "文章缩略图")
    private String cover;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 是否置顶
     */
    @Schema(description = "是否置顶")
    private Boolean isTop;

    /**
     * 文章状态
     */
    @Schema(description = "文章状态")
    private Integer state;

    public static void check(NoticeDTO noticeDTO) {
        if (!StringUtils.isSpecifiedLength(noticeDTO.getTitle(), 0, 20)) {
            throw new WarnException("标题长度超限");
        }
    }

    public static NoticeDTO of(Notice notice) {
        NoticeDTO noticeDTO = BeanCopyUtils.copyObject(notice, NoticeDTO.class);
        noticeDTO.setState(notice.getState().getCode());
        return noticeDTO;
    }
}
