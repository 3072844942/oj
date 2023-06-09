package org.oj.server.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.UserInfo;
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

    public static UserProfileVO of(UserInfo userInfo) {
        return BeanCopyUtils.copyObject(userInfo, UserProfileVO.class);
    }

    public static UserProfileVO of(UserInfoVO userInfo) {
        return BeanCopyUtils.copyObject(userInfo, UserProfileVO.class);
    }
}
