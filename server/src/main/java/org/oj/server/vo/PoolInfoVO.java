package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author march
 * @since 2023/6/12 下午4:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoolInfoVO {
    @Schema(description = "核心线程数")
    private Integer coreSize;

    @Schema(description = "最大线程数")
    private Integer maxSize;

    @Schema(description = "线程存活时间")
    private Long keepAliveTime;

    @Schema(description = "时间单位")
    private Integer unit;

    @Schema(description = "工作线程数")
    private Integer workSize;

    @Schema(description = "等待任务数")
    private Integer waitSize;
}
