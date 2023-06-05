package org.oj.server.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 聊天记录
 *
 * @author bin
 * @date 2021/07/29
 */
@Getter
@AllArgsConstructor
public class ChatRecordDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 用户登录ip
     */
    private String ipAddress;
}
