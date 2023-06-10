package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Contest;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * @author march
 * @since 2023/6/10 上午8:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestProfileVO {
    /**
     * 比赛ID
     */
    private String id;

    /**
     * 比赛标题
     */
    private String title;

    /**
     * 比赛创建人
     */
    private UserInfoVO author;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 是否报名
     */
    private Boolean isSignUp;

    /**
     * 比赛开始时间
     */
    private Long startTime;

    /**
     * 比赛结束时间
     */
    private Long endTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    public static ContestProfileVO of(Contest contest) {
        ContestProfileVO contestProfileVO = BeanCopyUtils.copyObject(contest, ContestProfileVO.class);
        contestProfileVO.setState(contest.getState().getCode());
        return contestProfileVO;
    }
}
