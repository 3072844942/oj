package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评测结果枚举
 *
 * @author march
 * @since 2023/5/31 上午10:36
 */
@Getter
@AllArgsConstructor
public enum JudgeStateEnum {
    // 答案错误，一般是程序正常运行，但是和期望输出不匹配
    WRONG_ANSWER("答案错误", 0),

    // 程序通过这个测试样例
    ACCEPTED("通过", 1),

    // 运行时错误
    RUNTIME_ERROR("运行时错误", 2),

    // 时间超限
    TIME_LIMIT_EXCEEDED("时间超限", 3),

    // 内存超限
    MEMORY_LIMIT_EXCEED("内存超限", 4),

    // 输出超限
    OUTPUT_LIMIT_EXCEED("输出超限", 5),

    // 段错误
    SEGMENTATION_FAULT("段错误", 6),

    // 浮点错误
    FLOAT_ERROR("浮点错误", 7),

    // 未知错误
    UNKNOWN_ERROR("未知错误", 8),

    // 找不到输入文件
    INPUT_FILE_NOT_FOUND("找不到输入文件", 9),

    // 无法寻找输出
    CAN_NOT_MAKE_OUTPUT("无法寻找输出", 10),

    // 设置限制错误
    SET_LIMIT_ERROR("设置限制错误", 11),

    // 非管理员用户
    NOT_ROOT_USER("非管理员用户", 12),

    //fork失败
    FORK_ERROR("fork失败", 13),

    //监控线程创建失败
    CREATE_THREAD_ERROR("监控线程创建失败", 14),

    //参数校验失败
    VALIDATE_ERROR("参数校验失败", 15),

    //编译失败
    COMPILE_ERROR("编译失败", 16);

    private final String desc;

    private final Integer code;
}
