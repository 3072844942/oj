package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

/**
 * 邮件信息
 *
 * @author march
 * @since 2023/6/5 下午4:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "邮件信息")
public class EmailDTO {
    /**
     * 收件人
     */
    @Schema(description = "收件人邮箱")
    private String email;

    /**
     * 主题
     */
    @Schema(description = "主题")
    private String subject;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;
}
