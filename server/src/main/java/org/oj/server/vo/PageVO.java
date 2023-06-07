package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页
 *
 * @author march
 * @since 2023/6/2 上午9:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页")
public class PageVO<T> {
    /**
     * 数据
     */
    @Schema(description = "数据")
    private List<T> list;

    /**
     * 总数
     */
    @Schema(description = "总数")
    private Long total;
}