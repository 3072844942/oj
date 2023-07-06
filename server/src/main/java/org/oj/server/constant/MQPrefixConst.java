package org.oj.server.constant;

/**
 * mq常量
 *
 * @author bin
 * @date 2021/07/28
 */
public class MQPrefixConst {
    /**
     * email交换机
     */
    public static final String EMAIL_EXCHANGE = "email_exchange";

    /**
     * 邮件队列
     */
    public static final String EMAIL_QUEUE = "email_queue";


    /**
     * 比赛缓冲交换机
     */
    public static final String CONTEXT_EXCHANGE = "context_exchange";

    /**
     * 比赛缓冲队列
     */
    public static final String CONTEXT_QUEUE = "context_queue";

    /**
     * 比赛缓冲死信队列
     */
    public static final String CONTEXT_DEAD_QUEUE = "context_dead_queue";

    public static final String CONTEXT_DEAD_EXCHANGE = "context_exchange_exchange";
}
