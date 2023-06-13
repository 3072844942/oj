package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Record;
import org.oj.server.util.BeanCopyUtils;

/**
 * @author march
 * @since 2023/6/12 下午5:24
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordVO {
    /**
     * id
     */
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
    private String contestId;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 评测结果
     */
    private Integer result;

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
    private Integer state;

    /**
     * 创建时间
     */
    private Long createTime;

    public static RecordVO of(Record record) {
        RecordVO recordVO = BeanCopyUtils.copyObject(record, RecordVO.class);
        recordVO.setResult(record.getResult().getCode());
        recordVO.setState(record.getState().getCode());
        return recordVO;
    }
}
