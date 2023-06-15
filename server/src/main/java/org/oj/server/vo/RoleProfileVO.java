package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Role;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

/**
 * @author march
 * @since 2023/6/14 上午9:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleProfileVO {
    /**
     * 角色id
     */
    private String id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 是否禁用
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

    public static RoleProfileVO of(Role role) {
        RoleProfileVO roleProfileVO = BeanCopyUtils.copyObject(role, RoleProfileVO.class);
        roleProfileVO.setState(role.getState().getCode());
        return roleProfileVO;
    }
}
