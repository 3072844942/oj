package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Article;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * 完整文章
 *
 * @author march
 * @since 2023/6/6 下午3:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVO {

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

    /**
     * 上一篇文章
     */
    private ArticlePaginationVO lastArticle;

    /**
     * 下一篇文章
     */
    private ArticlePaginationVO nextArticle;

    /**
     * 最新文章列表
     */
    private List<ArticleRecommendVO> newestArticleList;

    public static ArticleVO of(Article article) {
        ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
        articleVO.setStatus(article.getStatus().getCode());
        return articleVO;
    }
}
