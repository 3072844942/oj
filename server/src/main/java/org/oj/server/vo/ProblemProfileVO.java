package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Problem;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * @author march
 * @since 2023/7/6 上午10:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目简略信息")
public class ProblemProfileVO {
    /**
     * 题目ID
     */
    @Schema(description = "id")
    private String id;

    /**
     * 作者
     */
    @Schema(description = "作者")
    private UserProfileVO author;

    /**
     * 题目标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 题目标签列表
     */
    @Schema(description = "标签")
    private List<TagVO> tags;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer state;

    /**
     * 题目创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Long updateTime;

    public static ProblemProfileVO of(Problem problem) {
        ProblemProfileVO problemVO = BeanCopyUtils.copyObject(problem, ProblemProfileVO.class);
        problemVO.setState(problem.getState().getCode());
        return problemVO;
    }
}
