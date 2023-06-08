package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oj.server.entity.Article;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;

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
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章信息")
public class ArticleDTO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 作者
     */
    @Schema(description = "作者")
    private String userId;

    /**
     * 文章缩略图
     */
    @Schema(description = "缩略图")
    private String cover;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 摘要
     */
    @Schema(description = "摘要")
    private String summary;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 是否置顶
     */
    @Schema(description = "置顶")
    private Boolean isTop;

    /**
     * 标签Id
     */
    @Schema(description = "标签组")
    private List<String> tagIds;

    /**
     * 分类id
     */
    @Schema(description = "分类id")
    private String categoryId;

    /**
     * 文章状态
     */
    @Schema(description = "文章状态")
    private Integer state;

    /**
     * 检查合法性
     * @param articleDTO
     * @return 错误， 或者null
     */
    public static WarnException check(ArticleDTO articleDTO) {
        return null;
    }

    public static ArticleDTO of(Article article) {
        ArticleDTO articleDTO = BeanCopyUtils.copyObject(article, ArticleDTO.class);
        articleDTO.setState(article.getState().getCode());
        return articleDTO;
    }
}
