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
 * @since 2023/6/12 下午3:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目信息")
public class ProblemSearchVO {
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
     * 题目描述
     */
    @Schema(description = "内容")
    private String context;

    /**
     * 运行时间限制 ms
     */
    @Schema(description = "运行时间限制")
    private Integer timeRequire;

    /**
     * 内存限制 kb
     */
    @Schema(description = "内存限制")
    private Integer memoryRequire;

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

    public static ProblemSearchVO of(Problem problem) {
        ProblemSearchVO problemVO = BeanCopyUtils.copyObject(problem, ProblemSearchVO.class);
        problemVO.setState(problem.getState().getCode());
        return problemVO;
    }
}
