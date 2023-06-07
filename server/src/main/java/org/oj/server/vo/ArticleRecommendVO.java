package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Article;
import org.oj.server.util.BeanCopyUtils;

import java.time.LocalDateTime;

/**
 * 推荐文章
 *
 * @author bin
 * @date 2021/08/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleRecommendVO {

    /**
     * id
     */
    private Integer id;

    /**
     * 文章缩略图
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 更新时间
     */
    private Long updateTime;

    public static ArticleRecommendVO of(Article article) {
        return BeanCopyUtils.copyObject(article, ArticleRecommendVO.class);
    }
}
