package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.ProblemState;
import org.oj.server.entity.RankInfo;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;
import java.util.Map;

/**
 * @author march
 * @since 2023/6/10 上午8:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankInfoVO {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 罚时
     */
    private String penalty;

    /**
     * 题目状态
     * problemid - state
     */
    private List<ProblemStateVO> problemStates;

    public static RankInfoVO of(RankInfo rankInfo) {
        return BeanCopyUtils.copyObject(rankInfo, RankInfoVO.class);
    }
}
