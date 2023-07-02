package org.oj.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 撤回消息dto
 *
 * @author bin
 * @date 2021/08/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecallMessageDTO {

    /**
     * 消息id
     */
    private String id;

    /**
     * 是否为语音
     */
    private Boolean isVoice;

}
