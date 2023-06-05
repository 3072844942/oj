package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.EntityPermissionEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 文章
 *
 * @author bin
 * @date 2021/07/29
 * @since 2020-05-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("article")
public class Article {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 作者
     */
    private String userId;

    /**
     * 标签Id
     */
    private List<String> tagIds;

    /**
     * 分类id
     */
    private String categoryId;

    /**
     * 文章缩略图
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 文章状态
     */
    private EntityStateEnum status;

    /**
     * 权限
     */
    private EntityPermissionEnum permission;

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

    public static Article of(ArticleDTO articleDTO) {
        Article article = BeanCopyUtils.copyObject(articleDTO, Article.class);
        article.setStatus(EntityStateEnum.valueOf(articleDTO.getState()));
        article.setPermission(EntityPermissionEnum.valueOf(articleDTO.getPermission()));
        return article;
    }
}
