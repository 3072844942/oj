package org.oj.server.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 角色
 *
 * @author bin
 * @date 2021/08/01
 */
@Getter
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
     * 角色标签
     */
    private String roleLabel;

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
    private List<String> resourceIds;
}
