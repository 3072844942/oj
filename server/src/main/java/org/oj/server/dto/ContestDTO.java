package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oj.server.entity.Contest;
import org.oj.server.entity.RankInfo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

/**
 * 比赛
 *
 * @author march
 * @since 2023/5/31 上午10:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "比赛")
public class ContestDTO {
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
    @Schema(description = "创建角色id")
    private String userId;

    /**
     * 比赛描述
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 比赛题目集
     */
    @Schema(description = "题目集")
    private List<String> problemIds;

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

    public static WarnException check(ContestDTO contestDTO) {
        return null;
    }

    public static ContestDTO of(Contest contest) {
        ContestDTO contestDTO = BeanCopyUtils.copyObject(contest, ContestDTO.class);
        contestDTO.setState(contest.getState().getCode());
        return contestDTO;
    }
}
