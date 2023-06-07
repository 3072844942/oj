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
 * 后台文章
 *
 * @author march
 * @since 2023/6/6 下午4:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "后台文章列表信息")
public class ArticleBackVO {

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
    @Schema(description = "作者")
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
     * 是否置顶
     */
    @Schema(description = "置顶")
    private Boolean isTop;

    public static ArticleBackVO of(Article article) {
        ArticleBackVO articleBackVO = BeanCopyUtils.copyObject(article, ArticleBackVO.class);
        articleBackVO.setState(article.getState().getCode());
        return articleBackVO;
    }
}
