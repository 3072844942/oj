package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Notice;
import org.oj.server.util.BeanCopyUtils;

/**
 * @author march
 * @since 2023/6/12 上午10:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "公告搜索信息")
public class NoticeSearchVO {
    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 作者
     */
    @Schema(description = "作者")
    private UserProfileVO author;

    /**
     * 文章缩略图
     */
    @Schema(description = "缩略图")
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
    @Schema(description = "状态")
    private Integer state;

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

    public static NoticeSearchVO of(Notice notice) {
        NoticeSearchVO noticeVO = BeanCopyUtils.copyObject(notice, NoticeSearchVO.class);
        noticeVO.setState(notice.getState().getCode());
        return noticeVO;
    }
}
