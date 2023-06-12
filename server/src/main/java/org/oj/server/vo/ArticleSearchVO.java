package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "首页文章列表信息")
public class ArticleSearchVO {

    /**
     * 文章状态
     */
    @Schema(description = "状态")
    protected Integer state;
    /**
     * 修改时间
     */
    @Schema(description = "更新时间")
    protected Long updateTime;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    protected Long creatTime;
    /**
     * id
     */
    @Schema(description = "id")
    private String id;
    /**
     * 作者
     */
    @Schema(description = "作者id")
    private UserProfileVO author;
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
     * 是否置顶
     */
    @Schema(description = "置顶")
    private Boolean isTop;
    /**
     * 标签Id
     */
    @Schema(description = "标签id")
    private List<TagVO> tags;
    /**
     * 分类id
     */
    @Schema(description = "分类id")
    private CategoryVO category;

    public static ArticleSearchVO of(Article article) {
        ArticleSearchVO articleHomeVO = BeanCopyUtils.copyObject(article, ArticleSearchVO.class);
        articleHomeVO.setState(article.getState().getCode());
        return articleHomeVO;
    }
}
