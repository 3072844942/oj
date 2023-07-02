package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.dto.RoleDTO;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
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
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConst.ROLE)
public class Role {

    /**
     * 角色id
     */
    @Id
    private String id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 是否禁用
     */
    private EntityStateEnum state;

    /**
     * 菜单id
     */
    private List<String> menuIds;

    /**
     * 权限id
     */
    private Map<String, PermissionEnum> permissionIds;

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

    public static Role of(RoleDTO roleDTO) {
        Role role = BeanCopyUtils.copyObject(roleDTO, Role.class);
        role.setState(EntityStateEnum.valueOf(roleDTO.getState()));
        Map<String, PermissionEnum> permissionEnumMap = new HashMap<>();
        for (int i = 0; i < roleDTO.getPermissionIds().size(); i ++ ) {
            permissionEnumMap.put(
                    roleDTO.getPermissionIds().get(i),
                    PermissionEnum.valueOf(roleDTO.getPermissionStates().get(i))
            );
        }
        role.setPermissionIds(permissionEnumMap);
        return role;
    }
}
