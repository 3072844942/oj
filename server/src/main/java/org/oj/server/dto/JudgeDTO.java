package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author march
 * @since 2023/6/12 下午4:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评测信息")
public class JudgeDTO {
    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "题目id")
    private String problemId;

    @Schema(description = "比赛id")
    private String contestId;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "语言")
    private Integer language;

    @Schema(description = "测试输入")
    private String input;
}
