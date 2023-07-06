package org.oj.server.config;

import org.oj.server.constant.MQPrefixConst;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Rabbitmq配置类
 *
 * @author bin
 * @date 2021/07/29
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue emailQueue() {
        return new Queue(MQPrefixConst.EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(MQPrefixConst.EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

    @Bean
    public Queue contextQueue() {
        Map<String, Object> args = new HashMap<>();
        // x-dead-letter-exchange 这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", MQPrefixConst.CONTEXT_DEAD_EXCHANGE);
        // x-dead-letter-routing-key 这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", MQPrefixConst.CONTEXT_DEAD_QUEUE);
        return new Queue(MQPrefixConst.CONTEXT_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue deadContestQueue() {
        return new Queue(MQPrefixConst.CONTEXT_DEAD_QUEUE, true);
    }

    @Bean
    public FanoutExchange contextExchange() {
        return new FanoutExchange(MQPrefixConst.CONTEXT_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange deadContestExchange() {
        return new FanoutExchange(MQPrefixConst.CONTEXT_DEAD_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingContextDirect() {
        return BindingBuilder.bind(contextQueue()).to(contextExchange());
    }

    @Bean
    public Binding bindingDeadContextDirect() {
        return BindingBuilder.bind(deadContestQueue()).to(deadContestExchange());
    }
}
