package org.oj.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.LoginTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 用户账号
 *
 * @author bin
 * @date 2021/08/01
 * @since 2020-05-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "账户密码")
public class UsernamePassword {

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 验证码
     */
    @Schema(description = "验证码")
    private String code;
}
