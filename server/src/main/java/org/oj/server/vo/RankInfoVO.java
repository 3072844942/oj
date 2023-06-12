package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "排名信息")
public class RankInfoVO {
    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private String userId;

    /**
     * 罚时
     */
    @Schema(description = "总罚时")
    private String penalty;

    /**
     * 题目状态
     * problemid - state
     */
    @Schema(description = "题目状态")
    private List<ProblemStateVO> problemStates;

    public static RankInfoVO of(RankInfo rankInfo) {
        return BeanCopyUtils.copyObject(rankInfo, RankInfoVO.class);
    }
}
