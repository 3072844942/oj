package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oj.server.entity.Tag;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 文章标签
 * 标签
 *
 * @author xiaojie
 * @date 2021/07/29
 * @since 2020-05-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签信息")
public class TagDTO {

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

    public static void check(TagDTO tagDTO) {
        if (!StringUtils.isSpecifiedLength(tagDTO.getName(), 0, 20)) {
            throw new WarnException("名称长度超限");
        }
    }

    public static TagDTO of(Tag tag) {
        return BeanCopyUtils.copyObject(tag, TagDTO.class);
    }
}