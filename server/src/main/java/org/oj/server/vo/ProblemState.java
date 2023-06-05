package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author march
 * @since 2023/5/31 上午11:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemState {
    /**
     * 题目id
     */
    private String problemId;

    /**
     * 尝试次数
     */
    private Integer number;

    /**
     * 单题罚时
     */
    private Integer penalty;

    /**
     * 是否通过
     */
    private Boolean isAccept;

    /**
     * 是否一血
     */
    private Boolean firstBlood;
}
