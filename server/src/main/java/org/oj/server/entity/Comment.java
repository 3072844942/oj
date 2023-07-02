package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.dto.CommentDTO;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.SensitiveUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 评论
 *
 * @author bin
 * @date 2021/07/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConst.COMMENT)
public class Comment {

    /**
     * id
     */
    @Id
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
    private EntityStateEnum state;

    /**
     * 创建时间
     */
    @CreatedDate
    private Long createTime;

    public static Comment of(CommentDTO commentDTO) {
        Comment comment = BeanCopyUtils.copyObject(commentDTO, Comment.class);

        comment.setState(EntityStateEnum.valueOf(commentDTO.getState()));

        comment.setContent(SensitiveUtils.filter(comment.getContent()));

        return comment;
    }
}
