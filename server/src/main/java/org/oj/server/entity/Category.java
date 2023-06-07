package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.dto.CategoryDTO;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
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
@AllArgsConstructor
@NoArgsConstructor
@Document("category")
public class Category {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 分类名
     */
    private String title;

    /**
     * 创建时间
     */
    @CreatedDate
    private Long createTime;

    public static Category of(CategoryDTO categoryDTO) {
        return BeanCopyUtils.copyObject(categoryDTO, Category.class);
    }
}
