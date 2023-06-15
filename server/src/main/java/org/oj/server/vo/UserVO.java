package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.User;
import org.oj.server.enums.LoginTypeEnum;
import org.oj.server.util.BeanCopyUtils;

import java.util.List;

/**
 * @author march
 * @since 2023/6/14 上午8:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    /**
     * 用户ID
     * 和auth共用
     */
    private String id;

    /**
     * 角色id
     */
    private List<RoleProfileVO> roles;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录类型
     */
    private Integer loginType;

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
    private FacultyVO faculty;

    /**
     * 信息审核
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    public static UserVO of(User userInfo) {
        UserVO userVO = BeanCopyUtils.copyObject(userInfo, UserVO.class);
        userVO.setState(userInfo.getState().getCode());
        userVO.setLoginType(userInfo.getLoginType().getType());
        return userVO;
    }
}
