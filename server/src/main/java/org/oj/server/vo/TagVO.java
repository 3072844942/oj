package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Tag;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.Id;

/**
 * @author march
 * @since 2023/6/8 下午4:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签")
public class TagVO {

    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 标签
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    public static TagVO of(Tag tag) {
        return BeanCopyUtils.copyObject(tag, TagVO.class);
    }
}
