package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * jni判题返回值
 *
 * @author march
 * @since 2023/7/10 下午4:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Long cpuTimeCost;
    private Long realTimeCost;
    private Long memoryCost;
    private Integer condition;
    private String outPath;
    private String inPath;
    private String errPath;
    private String loggerPath;
}
