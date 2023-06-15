package org.oj.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.User;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * 用户信息
 *
 * @author bin
 * @date 2021/08/01
 * @since 2020-05-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    /**
     * 用户ID
     * 和auth共用
     */
    private String id;

    private List<String> roleIds;

    /**
     * 邮箱号
     */
    private String email;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String intro;

    /**
     * 个人网站
     */
    private String webSite;

    /**
     * 是否禁言
     */
    private Boolean isDisable;

    /**
     * 学号
     */
    private String number;

    /**
     * 名字
     */
    private String name;

    /**
     * 学院id
     */
    private String facultyId;

    /**
     * 信息审核
     */
    private Integer state;

    public static UserInfoDTO of(User userInfo) {
        UserInfoDTO userInfoDTO = BeanCopyUtils.copyObject(userInfo, UserInfoDTO.class);
        userInfoDTO.setState(userInfo.getState().getCode());
        return userInfoDTO;
    }
}
