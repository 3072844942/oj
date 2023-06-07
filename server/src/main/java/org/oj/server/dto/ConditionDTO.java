package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Schema(description = "页码")
    private Integer current = 1;
    /**
     * 条数
     */
    @Schema(description = "条数")
    private Integer size = 10;
    /**
     * 搜索内容
     */
    @Schema(description = "内容")
    private String keywords = "";

    /**
     * 标签要求
     */
    @Schema(description = "条数")
    private List<String> tags = new ArrayList<>();

    @Schema(description = "id")
    private String id = "";

    @Schema(description = "用户id")
    private String userId = "";

    @Schema(description = "id集合")
    private List<String> ids = new ArrayList<>();
}
