package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.SensitiveUtils;
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
     * 文章状态
     */
    protected EntityStateEnum state;
    /**
     * 修改时间
     */
    @LastModifiedDate
    protected Long updateTime;
    /**
     * 创建时间
     */
    @CreatedDate
    protected Long creatTime;
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
     * 摘要
     */
    private String summary;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否置顶
     */
    private Boolean isTop;

    public static Article of(ArticleDTO articleDTO) {
        Article article = BeanCopyUtils.copyObject(articleDTO, Article.class);
        // 过滤文本
        article.setContent(SensitiveUtils.filter(article.getContent()));
        article.setSummary(SensitiveUtils.filter(article.getSummary()));
        article.setTitle(SensitiveUtils.filter(article.getTitle()));
        // 设置权限
        article.setState(EntityStateEnum.valueOf(articleDTO.getState()));
        return article;
    }
}