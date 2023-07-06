package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Contest;
import org.oj.server.entity.RankInfo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.List;
import java.util.Map;

/**
 * 前台比赛信息
 *
 * @author march
 * @since 2023/6/9 下午3:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "比赛信息")
public class ContestInfoVO {
    /**
     * 比赛ID
     */
    @Schema(description = "id")
    private String id;

    /**
     * 比赛标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 比赛创建人
     */
    @Schema(description = "作者信息")
    private UserProfileVO author;

    /**
     * 比赛描述
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 比赛题目集
     */
    @Schema(description = "题目集")
    private List<ProblemProfileVO> problems;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer state;

    /**
     * 比赛开始时间
     */
    @Schema(description = "开始时间")
    private Long startTime;

    /**
     * 比赛结束时间
     */
    @Schema(description = "结束时间")
    private Long endTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Long updateTime;

    public static ContestInfoVO of(Contest contest) {
        ContestInfoVO contestInfoVO = BeanCopyUtils.copyObject(contest, ContestInfoVO.class);
        contestInfoVO.setState(contest.getState().getCode());
        return contestInfoVO;
    }
}
