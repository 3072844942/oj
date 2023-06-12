package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Contest;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * 简略比赛信息
 *
 * @author march
 * @since 2023/6/10 上午8:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "简略比赛信息")
public class ContestProfileVO {
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
    @Schema(description = "作者")
    private UserProfileVO author;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer state;

    /**
     * 是否报名
     */
    @Schema(description = "是否报名")
    private Boolean isSignUp;

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

    public static ContestProfileVO of(Contest contest) {
        ContestProfileVO contestProfileVO = BeanCopyUtils.copyObject(contest, ContestProfileVO.class);
        contestProfileVO.setState(contest.getState().getCode());
        return contestProfileVO;
    }
}
