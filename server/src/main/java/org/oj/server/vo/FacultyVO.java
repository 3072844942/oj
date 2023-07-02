package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Faculty;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.Id;

/**
 * @author march
 * @since 2023/6/9 上午9:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "学院信息")
public class FacultyVO {
    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 学院名称
     */
    @Schema(description = "名称")
    private String title;

    /**
     * 学院描述
     */
    @Schema(description = "描述")
    private String content;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    public static FacultyVO of(Faculty faculty) {
        return BeanCopyUtils.copyObject(faculty, FacultyVO.class);
    }
}
