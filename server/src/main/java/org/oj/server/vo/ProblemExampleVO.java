package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.ProblemExample;
import org.oj.server.util.BeanCopyUtils;

/**
 * @author march
 * @since 2023/6/9 下午3:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目阳历")
public class ProblemExampleVO {
    /**
     * 输入样例
     */
    @Schema(description = "输入")
    private String test;
    /**
     * 输出样例
     */
    @Schema(description = "输出")
    private String answer;

    public static ProblemExampleVO of(ProblemExample problemExample) {
        return BeanCopyUtils.copyObject(problemExample, ProblemExampleVO.class);
    }
}
