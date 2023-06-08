package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Article;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.Id;

import java.util.List;


/**
 * 搜索文章
 *
 * @author bin
 * @date 2021/08/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchDTO {

    /**
     * 文章id
     */
    private String id;

    /**
     * 作者id
     */
    private String userId;

    /**
     * 分类id
     */
    private String categoryId;

    /**
     * 标签id
     */
    private List<String> tagIds;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章状态
     */
    private Integer state;

    public static ArticleSearchDTO of(Article article) {
        ArticleSearchDTO articleSearchDTO = BeanCopyUtils.copyObject(article, ArticleSearchDTO.class);
        articleSearchDTO.setState(article.getState().getCode());
        return articleSearchDTO;
    }
}
