package org.oj.server.dto;

import lombok.*;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户信息
 *
 * @author bin
 * @date 2021/08/01
 * @since 2020-05-18
 */
@Getter
@AllArgsConstructor
public class UserInfoDTO {

    /**
     * 用户ID
     * 和auth共用
     */
    private String id;

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
    private Integer isDisable;

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
    private Integer status;
}
