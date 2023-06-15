package org.oj.server.dto;

import io.netty.util.internal.StringUtil;
import lombok.*;
import org.oj.server.entity.Role;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * 角色
 *
 * @author bin
 * @date 2021/08/01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

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

    public static void check(RoleDTO roleDTO) {
        if (roleDTO.getPermissionIds().size() != roleDTO.getPermissionStates().size()) {
            throw new ErrorException("权限状态不匹配");
        }
        if (!StringUtils.isSpecifiedLength(roleDTO.getName(), 0, 20)) {
            throw new ErrorException("名字长度超限");
        }
    }

    public static RoleDTO of(Role role) {
        RoleDTO roleDTO = BeanCopyUtils.copyObject(role, RoleDTO.class);
        roleDTO.setPermissionIds(role.getPermissionIds().keySet().stream().toList());
        roleDTO.setPermissionStates(role.getPermissionIds().values().stream().map(PermissionEnum::getCode).toList());
        return roleDTO;
    }
}
