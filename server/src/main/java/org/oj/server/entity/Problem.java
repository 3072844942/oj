package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.dto.ProblemDTO;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.SensitiveUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConst.PROBLEM)
public class Problem {
    /**
     * 题目ID
     */
    @Id
    private String id;

    /**
     * 作者id
     */
    private String userId;

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
    private List<ProblemExample> examples;

    /**
     * 提示
     */
    private String intro;

    /**
     * 运行时间限制 ms
     */
    private Long timeRequire;

    /**
     * 内存限制 kb
     */
    private Long memoryRequire;

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
    private EntityStateEnum state;

    /**
     * 题目创建时间
     */
    @CreatedDate
    private Long createTime;

    /**
     * 更新时间
     */
    @LastModifiedDate
    private Long updateTime;

    public static Problem of(ProblemDTO problemDTO) {
        Problem problem = BeanCopyUtils.copyObject(problemDTO, Problem.class);

        problem.setContent(SensitiveUtils.filter(problemDTO.getContent()));
        problem.setTitle(SensitiveUtils.filter(problemDTO.getTitle()));
        problem.setIntro(SensitiveUtils.filter(problemDTO.getIntro()));
        problem.setInputContent(SensitiveUtils.filter(problemDTO.getInputContent()));
        problem.setOutputContent(SensitiveUtils.filter(problemDTO.getOutputContent()));

        problem.setState(EntityStateEnum.valueOf(problemDTO.getState()));
        return problem;
    }
}
