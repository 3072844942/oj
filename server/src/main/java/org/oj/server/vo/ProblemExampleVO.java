package org.oj.server.vo;

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
public class ProblemExampleVO {
    /**
     * 输入样例
     */
    private String test;
    /**
     * 输出样例
     */
    private String answer;

    public static ProblemExampleVO of(ProblemExample problemExample) {
        return BeanCopyUtils.copyObject(problemExample, ProblemExampleVO.class);
    }
}
