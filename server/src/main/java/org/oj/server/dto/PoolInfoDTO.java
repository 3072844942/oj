package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author march
 * @since 2023/6/12 下午4:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "线程池信息")
public class PoolInfoDTO {
    @Schema(description = "核心线程数")
    private Integer corePoolSize;

    @Schema(description = "最大线程数")
    private Integer maxPoolSize;

    @Schema(description = "线程存活时间")
    private Long keepAliveTime;

    @Schema(description = "时间单位")
    private Integer unit;
}
