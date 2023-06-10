package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.ProblemState;
import org.oj.server.util.BeanCopyUtils;

/**
 * @author march
 * @since 2023/6/10 上午8:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemStateVO {
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

    public static ProblemStateVO of(ProblemState problemState) {
        return BeanCopyUtils.copyObject(problemState, ProblemStateVO.class);
    }
}