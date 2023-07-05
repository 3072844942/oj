package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Message;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

/**
 * @author march
 * @since 2023/7/3 下午3:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageVO {
    /**
     * 主键id
     */
    private String id;

    /**
     * 发布人id
     */
    private String userId;

    /**
     * 接受人id
     */
    private String toUserId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Long createTime;

    private Long updateTime;

    public static MessageVO of(Message message) {
        MessageVO messageVO = BeanCopyUtils.copyObject(message, MessageVO.class);
        messageVO.setState(message.getState().getCode());
        return messageVO;
    }
}
