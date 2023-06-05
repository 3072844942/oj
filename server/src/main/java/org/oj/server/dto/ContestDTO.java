package org.oj.server.dto;

import lombok.*;

import java.util.List;

/**
 * 比赛
 *
 * @author march
 * @since 2023/5/31 上午10:28
 */
@Getter
@AllArgsConstructor
public class ContestDTO {
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
    private String userId;

    /**
     * 比赛描述
     */
    private String context;

    /**
     * 比赛题目集
     */
    private List<String> problemIds;

    /**
     * 加入的用户集合
     */
    private List<String> userIds;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 权限
     */
    private Integer permission;
}
