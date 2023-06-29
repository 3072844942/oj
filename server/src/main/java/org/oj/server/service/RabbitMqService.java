package org.oj.server.service;

import com.alibaba.fastjson.JSON;
import org.oj.server.constant.MQPrefixConst;
import org.oj.server.dto.EmailDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author march
 * @since 2023/6/5 下午4:42
 */
@Service
public class RabbitMqService {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送验证码
     * @param email 收件人
     * @param code 验证码
     * @param minuter 过期时间 / 分钟
     */
    public void email(String email, String code, Long minuter) {
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject("验证码")
                .content("您的验证码为 " + code + " 有效期" + minuter + "分钟，请不要告诉他人哦！")
                .build();
        rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
    }
}
