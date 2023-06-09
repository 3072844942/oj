package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Article;
import org.oj.server.util.BeanCopyUtils;

/**
 * 文章上下篇
 *
 * @author bin
 * @date 2021/07/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "文章上下篇")
public class ArticlePaginationVO {

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


    public static ArticlePaginationVO of(Article article) {
        return BeanCopyUtils.copyObject(article, ArticlePaginationVO.class);
    }
}
