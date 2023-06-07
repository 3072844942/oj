package org.oj.server.vo;

import lombok.*;
import org.oj.server.entity.Article;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * 后台文章
 *
 * @author march
 * @since 2023/6/6 下午4:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBackVO {

    /**
     * id
     */
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
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 文章状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    public static ArticleBackVO of(Article article) {
        ArticleBackVO articleBackVO = BeanCopyUtils.copyObject(article, ArticleBackVO.class);
        articleBackVO.setStatus(article.getStatus().getCode());
        return articleBackVO;
    }
}
