package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Role;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.List;
import java.util.Map;

/**
 * @author march
 * @since 2023/6/14 上午9:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO {
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
     * 菜单id
     */
    private List<String> menuIds;

    /**
     * 权限id
     */
    private List<String> permissionIds;
    private List<Integer> permissionStates;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    public static RoleVO of(Role role) {
        RoleVO roleVO = BeanCopyUtils.copyObject(role, RoleVO.class);
        roleVO.setPermissionIds(role.getPermissionIds().keySet().stream().toList());
        roleVO.setPermissionStates(role.getPermissionIds().values().stream().map(PermissionEnum::getCode).toList());
        return roleVO;
    }
}
