package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登陆类型枚举
 *
 * @author march
 * @since 2023/5/31 上午10:01
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 邮箱登录
     */
    EMAIL(1, "邮箱登录", ""),
    /**
     * QQ登录
     */
    QQ(2, "QQ登录", "qqLoginStrategyImpl"),
    /**
     * 微博登录
     */
    WEIBO(3, "微博登录", "weiboLoginStrategyImpl"),
    /**
     * 微信登录
     **/
    WX(4, "微信登录", "wxLoginStrategyImpl");

    /**
     * 登录方式
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 策略
     */
    private final String strategy;
}
