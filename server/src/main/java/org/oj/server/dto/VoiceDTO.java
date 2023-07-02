package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 声音签证官
 *
 * @author bin
 * @date 2021/07/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "音频")
public class VoiceDTO {

    /**
     * 消息类型
     */
    @Schema(description = "type")
    private Integer type;

    /**
     * 文件
     */
    @Schema(description = "file")
    private MultipartFile file;

    /**
     * 用户id
     */
    @Schema(description = "userId")
    private Integer userId;

    /**
     * 用户昵称
     */
    @Schema(description = "nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @Schema(description = "avatar")
    private String avatar;

    /**
     * 聊天内容
     */
    @Schema(description = "content")
    private String content;

    /**
     * 创建时间
     */
    @Schema(description = "createTime")
    private Date createTime;

    /**
     * 用户登录ip
     */
    @Schema(description = "ipAddress")
    private String ipAddress;

    /**
     * ip来源
     */
    @Schema(description = "ipSource")
    private String ipSource;

}
