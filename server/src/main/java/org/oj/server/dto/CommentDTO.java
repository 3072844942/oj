package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oj.server.entity.Comment;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 评论
 *
 * @author bin
 * @date 2021/07/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论信息")
public class CommentDTO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 评论用户Id
     */
    @Schema(description = "评论用户id")
    private String userId;

    /**
     * 回复用户id
     */
    @Schema(description = "回复用户id")
    private String replyUserId;

    /**
     * 评论文章id
     */
    @Schema(description = "文章id")
    private String articleId;

    /**
     * 评论内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 父评论id
     */
    @Schema(description = "父评论id")
    private String parentId;

    /**
     * 是否审核
     */
    @Schema(description = "是否审核")
    private Integer state;

    public static void check(CommentDTO commentDTO) {
        if (!StringUtils.isSpecifiedLength(commentDTO.getContent(), 0, 1000)) {
            throw new WarnException("内容长度超限");
        }
    }

    public static CommentDTO of(Comment comment) {
        CommentDTO commentDTO = BeanCopyUtils.copyObject(comment, CommentDTO.class);
        commentDTO.setState(comment.getState().getCode());
        return commentDTO;
    }
}
