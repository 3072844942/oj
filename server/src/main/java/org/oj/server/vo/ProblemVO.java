package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Problem;
import org.oj.server.entity.ProblemExample;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @author march
 * @since 2023/6/9 下午3:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "题目信息")
public class ProblemVO {
    /**
     * 题目ID
     */
    @Schema(description = "id")
    private String id;

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
     * 题目输入描述
     */
    @Schema(description = "输入描述")
    private String inputContext;

    /**
     * 题目输出描述
     */
    @Schema(description = "输出描述")
    private String outputContext;

    /**
     * 题目样例
     */
    @Schema(description = "样例")
    private List<ProblemExampleVO> examples;

    /**
     * 提示
     */
    @Schema(description = "提示")
    private String desc;

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

    public static ProblemVO of(Problem problem) {
        ProblemVO problemVO = BeanCopyUtils.copyObject(problem, ProblemVO.class);
        problemVO.setState(problem.getState().getCode());
        return problemVO;
    }
}
