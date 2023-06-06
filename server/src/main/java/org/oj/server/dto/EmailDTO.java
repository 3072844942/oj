package org.oj.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author march
 * @since 2023/6/5 下午4:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    /**
     * 收件人
     */
    private String email;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;
}
