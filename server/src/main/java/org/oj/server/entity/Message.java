package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.dto.MessageDTO;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.SensitiveUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 留言
 *
 * @author bin
 * @date 2021/08/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConst.MESSAGE)
public class Message {
    /**
     * 主键id
     */
    @Id
    private String id;

    /**
     * 发布人id
     */
    private String userId;

    /**
     * 留言内容
     */
    private String content;

    /**
     *
     */
    private EntityStateEnum state;

    /**
     * 创建时间
     */
    @CreatedDate
    private Long createTime;

    @LastModifiedDate
    private Long updateTime;

    public static Message of(MessageDTO messageDTO) {
        Message message = BeanCopyUtils.copyObject(messageDTO, Message.class);
        message.setState(EntityStateEnum.valueOf(messageDTO.getState()));
        message.setContent(SensitiveUtils.filter(messageDTO.getContent()));
        return message;
    }
}
