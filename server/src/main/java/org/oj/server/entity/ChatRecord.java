package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 聊天记录
 *
 * @author bin
 * @date 2021/07/29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConst.CHAT_RECORD)
public class ChatRecord {


    /**
     * 主键
     */
    @Id
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
     * 类型
     */
    private Integer type;

    /**
     * 用户登录ip
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private LocalDateTime updateTime;

}
