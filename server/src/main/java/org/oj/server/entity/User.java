package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.LoginTypeEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

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
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConst.USER)
public class User {

    /**
     * 用户ID
     */
    @Id
    private String id;

    /**
     * 角色id
     */
    private List<String> roleIds;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录类型
     */
    private LoginTypeEnum loginType;

    /**
     * 用户登录ip
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

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
    private EntityStateEnum state;

    /**
     * 创建时间
     */
    @CreatedDate
    private Long createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private Long updateTime;
}
