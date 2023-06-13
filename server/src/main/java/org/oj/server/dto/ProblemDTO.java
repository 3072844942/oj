package org.oj.server.dto;

import lombok.*;
import org.oj.server.entity.Problem;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 题目
 *
 * @author march
 * @since 2023/5/31 上午10:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDTO {
    /**
     * 题目ID
     */
    private String id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签列表
     */
    private List<String> tagIds;

    /**
     * 题目描述
     */
    private String content;

    /**
     * 题目输入描述
     */
    private String inputContent;

    /**
     * 题目输出描述
     */
    private String outputContent;

    /**
     * 题目样例
     */
    private List<ProblemExampleDTO> examples;

    /**
     * 提示
     */
    private String intro;

    /**
     * 运行时间限制 ms
     */
    private Integer timeRequire;

    /**
     * 内存限制 kb
     */
    private Integer memoryRequire;

    /**
     * 测试数据地址
     */
    private String address;

    /**
     * 是否特判
     */
    private Boolean isSpecial;

    /**
     * 特判程序地址
     */
    private String specialAddress;

    /**
     * 状态
     */
    private Integer state;

    public static void check(ProblemDTO problemDTO) {
        if (!StringUtils.isSpecifiedLength(problemDTO.getTitle(), 0, 20)) {
            throw new WarnException("标题长度超限");
        }
        if (!StringUtils.isSpecifiedLength(problemDTO.getAddress(), 0, 100)) {
            throw new WarnException("题目测试数据错误");
        }
        if (problemDTO.getIsSpecial() && !StringUtils.isSpecifiedLength(problemDTO.getSpecialAddress(), 0, 100)) {
            throw new WarnException("特判程序错误");
        }
    }

    public static ProblemDTO of(Problem problem) {
        ProblemDTO problemDTO = BeanCopyUtils.copyObject(problem, ProblemDTO.class);
        problemDTO.setState(problem.getState().getCode());
        return problemDTO;
    }
}
