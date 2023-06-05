package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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
     * 罚时
     */
    private String penalty;

    /**
     * 题目状态
     * problemid - state
     */
    private Map<String, ProblemState> problemStateMap;
}
