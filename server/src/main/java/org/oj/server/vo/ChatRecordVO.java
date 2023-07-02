package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.ChatRecord;

import java.util.List;


/**
 * 聊天记录
 *
 * @author bin
 * @date 2021/08/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRecordVO {

    /**
     * 聊天记录
     */
    private List<ChatRecord> chatRecordList;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

}
