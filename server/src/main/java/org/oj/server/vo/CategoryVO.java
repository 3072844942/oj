package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Category;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

/**
 * 分类
 *
 * @author march
 * @since 2023/6/7 下午8:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类")
public class CategoryVO {

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

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    public static CategoryVO of(Category category) {
        return BeanCopyUtils.copyObject(category, CategoryVO.class);
    }
}
