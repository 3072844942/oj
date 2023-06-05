package org.oj.server.dto;

import lombok.*;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 题目
 *
 * @author march
 * @since 2023/5/31 上午10:28
 */
@Getter
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
     * 题目等级
     */
    private String grade;

    /**
     * 题目标签列表
     */
    private List<String> tags;

    /**
     * 题目描述
     */
    private String context;

    /**
     * 题目输入描述
     */
    private String inputContext;

    /**
     * 题目输出描述
     */
    private String outputContext;

    /**
     * 题目样例
     */
    private List<ProblemExampleDTO> examples;

    /**
     * 提示
     */
    private String desc;

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
}
