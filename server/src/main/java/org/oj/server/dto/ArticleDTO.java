package org.oj.server.dto;

import lombok.*;
import org.oj.server.exception.WarnException;

import java.util.List;

/**
 * 文章
 *
 * @author bin
 * @date 2021/07/29
 * @since 2020-05-18
 */
@Getter
@Setter
@AllArgsConstructor
public class ArticleDTO {

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
     * 内容
     */
    private String content;

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

    /**
     * 文章状态
     */
    private Integer state;

    /**
     * 权限
     */
    private Integer permission;

    /**
     * 检查合法性
     * @param articleDTO
     * @return 错误， 或者null
     */
    public static WarnException check(ArticleDTO articleDTO) {
        return null;
    }
}
