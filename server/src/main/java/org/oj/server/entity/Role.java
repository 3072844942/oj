package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
@AllArgsConstructor
@NoArgsConstructor
@Document("role")
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
}
