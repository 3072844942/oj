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
 * @since 2023/6/9 上午9:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "完成用户信息")
public class UserInfoVO {

    /**
     * 用户ID
     * 和auth共用
     */
    @Schema(description = "id")
    private String id;

    /**
     * 邮箱号
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 用户昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 用户简介
     */
    @Schema(description = "介绍")
    private String intro;

    /**
     * 个人网站
     */
    @Schema(description = "个人网站")
    private String webSite;

    /**
     * 是否禁言
     */
    @Schema(description = "是否禁言")
    private Integer isDisable;

    /**
     * 学号
     */
    @Schema(description = "学号")
    private String number;

    /**
     * 名字
     */
    @Schema(description = "名字")
    private String name;

    /**
     * 学院
     */
    @Schema(description = "学院")
    private FacultyVO faculty;

    /**
     * 信息审核
     */
    @Schema(description = "信息审核")
    private Integer state;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Long createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private Long updateTime;

    public static UserInfoVO of(UserInfo userInfo) {
        UserInfoVO userInfoVO = BeanCopyUtils.copyObject(userInfo, UserInfoVO.class);
        userInfoVO.setState(userInfo.getState().getCode());
        return userInfoVO;
    }
}
