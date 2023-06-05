package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author march
 * @since 2023/5/31 上午10:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemExample {
    /**
     * 输入样例
     */
    private String test;
    /**
     * 输出样例
     */
    private String answer;
}
