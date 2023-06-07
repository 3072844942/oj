package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Article;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * 首页文章
 *
 * @author bin
 * @date 2021/07/29
 * @since 2020-05-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleHomeVO {

    /**
     * id
     */
    private String id;

    /**
     * 作者
     */
    private String userId;

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
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 标签Id
     */
    private List<String> tagIds;

    /**
     * 分类id
     */
    private String categoryId;

    public static ArticleHomeVO of(Article article) {
        return BeanCopyUtils.copyObject(article, ArticleHomeVO.class);
    }
}
