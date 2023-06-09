package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Comment;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * @author march
 * @since 2023/6/8 下午4:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论")
public class CommentVO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 评论用户Id
     */
    @Schema(description = "作者")
    private UserProfileVO author;

    /**
     * 回复用户id
     */
    @Schema(description = "回复用户")
    private UserProfileVO replyUser;

    /**
     * 评论内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 父评论id
     */
    @Schema(description = "子评论")
    private List<CommentVO> children;

    /**
     * 是否审核
     */
    @Schema(description = "信息状态")
    private Integer state;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    public static CommentVO of(Comment comment) {
        CommentVO commentVO = BeanCopyUtils.copyObject(comment, CommentVO.class);
        commentVO.setState(comment.getState().getCode());
        return commentVO;
    }
}
