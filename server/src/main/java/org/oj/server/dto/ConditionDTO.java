package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询条件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "搜索条件")
public class ConditionDTO {
    /**
     * 页码
     */
    @Builder.Default
    @Schema(description = "页码")
    private Integer current = 1;
    /**
     * 条数
     */
    @Builder.Default
    @Schema(description = "条数")
    private Integer size = 10;
    /**
     * 搜索内容
     */
    @Builder.Default
    @Schema(description = "内容")
    private String keywords = "";

    /**
     * 标签要求
     */
    @Builder.Default
    @Schema(description = "条数")
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    @Schema(description = "id")
    private String id = "";

    @Builder.Default
    @Schema(description = "用户id")
    private String userId = "";

    @Builder.Default
    @Schema(description = "id集合")
    private List<String> ids = new ArrayList<>();

    public static WarnException check(ConditionDTO conditionDTO) {
        if (conditionDTO.current < 0) {
            return new WarnException("页码超限");
        }
        if (conditionDTO.size < 0 || conditionDTO.size > 100) {
            return new WarnException("请求数量过大");
        }
        return null;
    }
}
