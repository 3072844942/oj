package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.JudgeStateEnum;
import org.oj.server.enums.EntityPermissionEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 评测记录
 *
 * @author march
 * @since 2023/5/31 上午10:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("record")
public class Record {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 题目id
     */
    private String problemId;

    /**
     * 比赛id
     */
    private String cotestId;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 评测结果
     */
    private JudgeStateEnum state;

    /**
     * 时间消耗
     */
    private Integer timeCost;

    /**
     * 内存消耗
     */
    private Integer memoryCost;

    /**
     * 权限
     */
    private EntityPermissionEnum permission;

    /**
     * 创建时间
     */
    private Long createTime;
}
