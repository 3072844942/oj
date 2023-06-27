package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.User;
import org.oj.server.util.BeanCopyUtils;

/**
 * @author march
 * @since 2023/6/8 下午4:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "简略用户信息")
public class UserProfileVO {
    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "学号")
    private String number;

    public static UserProfileVO of(User userInfo) {
        return BeanCopyUtils.copyObject(userInfo, UserProfileVO.class);
    }

    public static UserProfileVO of(UserVO userInfo) {
        return BeanCopyUtils.copyObject(userInfo, UserProfileVO.class);
    }
}
