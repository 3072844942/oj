package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.vo.ProblemStateVO;

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
    private Long penalty;

    /**
     * 是否通过
     */
    private Boolean isAccept;

    /**
     * 是否一血
     */
    private Boolean firstBlood;

    public static ProblemState of(ProblemStateVO problemStateVO) {
        return BeanCopyUtils.copyObject(problemStateVO, ProblemState.class);
    }
}
