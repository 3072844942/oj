package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Article;
import org.oj.server.util.BeanCopyUtils;

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
@Schema(description = "推荐文章")
public class ArticleRecommendVO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 文章缩略图
     */
    @Schema(description = "文章缩略图")
    private String cover;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Long updateTime;

    public static ArticleRecommendVO of(Article article) {
        return BeanCopyUtils.copyObject(article, ArticleRecommendVO.class);
    }
}
