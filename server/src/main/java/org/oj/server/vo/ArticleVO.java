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
 * 完整文章
 *
 * @author march
 * @since 2023/6/6 下午3:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "完整文章信息")
public class ArticleVO {

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
    private String userId;
    /**
     * 标签Id
     */
    @Schema(description = "标签id")
    private List<String> tagIds;
    /**
     * 分类id
     */
    @Schema(description = "分类id")
    private String categoryId;
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
     * 上一篇文章
     */
    @Schema(description = "上一篇文章")
    private ArticlePaginationVO last;
    /**
     * 下一篇文章
     */
    @Schema(description = "下一篇文章")
    private ArticlePaginationVO next;
    /**
     * 最新文章列表
     */
    @Schema(description = "最新文章")
    private List<ArticleRecommendVO> newesList;

    public static ArticleVO of(Article article) {
        ArticleVO articleVO = BeanCopyUtils.copyObject(article, ArticleVO.class);
        articleVO.setState(article.getState().getCode());
        return articleVO;
    }
}
