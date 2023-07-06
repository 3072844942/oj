package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.vo.RankInfoVO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 排名信息
 *
 * @author march
 * @since 2023/5/31 上午11:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankInfo {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 过题数
     */
    private Integer count;

    /**
     * 罚时
     */
    private Long penalty;

    /**
     * 题目状态
     * problemid - state
     */
    private Map<String, ProblemState> problemStateMap;

    public static RankInfo of(RankInfoVO infoVO) {
        RankInfo rankInfo = BeanCopyUtils.copyObject(infoVO, RankInfo.class);
        List<ProblemState> problemStates = infoVO.getProblemStates().stream().map(ProblemState::of).toList();

        rankInfo.setProblemStateMap(
                problemStates.stream().collect(Collectors.toMap(ProblemState::getProblemId, a -> a, (k1, k2) -> k1))
        );
        return rankInfo;
    }
}
