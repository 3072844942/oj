package org.oj.server.dto;

import lombok.*;
import org.oj.server.entity.Comment;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
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
public class CommentDTO {

    /**
     * id
     */
    private String id;

    /**
     * 评论用户Id
     */
    private String userId;

    /**
     * 回复用户id
     */
    private String replyUserId;

    /**
     * 评论文章id
     */
    private String articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论id
     */
    private String parentId;

    /**
     * 是否审核
     */
    private Integer state;

    public static WarnException check(CommentDTO commentDTO) {
        return null;
    }

    public static CommentDTO of(Comment comment) {
        CommentDTO commentDTO = BeanCopyUtils.copyObject(comment, CommentDTO.class);
        commentDTO.setState(comment.getState().getCode());
        return commentDTO;
    }
}
