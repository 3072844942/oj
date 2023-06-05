package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.EntityPermissionEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@AllArgsConstructor
@NoArgsConstructor
@Document("contest")
public class Contest {
    /**
     * 比赛ID
     */
    @Id
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
     * 排名
     * userid - info
     */
    private Map<String, RankInfo> rank;

    /**
     * 状态
     */
    private EntityStateEnum state;

    /**
     * 权限
     */
    private EntityPermissionEnum permission;

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
}
