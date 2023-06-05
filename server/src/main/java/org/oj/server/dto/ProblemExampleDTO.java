package org.oj.server.dto;

import lombok.*;

/**
 * @author march
 * @since 2023/5/31 上午10:30
 */
@Getter
@AllArgsConstructor
public class ProblemExampleDTO {
    /**
     * 输入样例
     */
    private String test;
    /**
     * 输出样例
     */
    private String answer;
}
