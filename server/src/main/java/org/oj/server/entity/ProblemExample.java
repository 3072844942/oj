package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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