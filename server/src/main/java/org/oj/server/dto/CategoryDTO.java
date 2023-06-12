package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oj.server.entity.Category;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 分类
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类")
public class CategoryDTO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 分类名
     */
    @Schema(description = "标题")
    private String title;

    public static void check(CategoryDTO categoryDTO) {
        if (!StringUtils.isSpecifiedLength(categoryDTO.getTitle(), 0, 20)) {
            throw new WarnException("标题长度超限");
        }
    }

    public static CategoryDTO of(Category category) {
        return BeanCopyUtils.copyObject(category, CategoryDTO.class);
    }
}
