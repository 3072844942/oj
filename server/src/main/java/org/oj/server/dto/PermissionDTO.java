package org.oj.server.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 资源
 *
 * @author bin
 * @date 2021/08/01
 */
@Getter
@AllArgsConstructor
public class PermissionDTO {
    /**
     * 权限id
     */
    private String id;

    /**
     * 权限名
     */
    private String title;

    /**
     * 权限路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 父权限id
     */
    private Integer parentId;

    /**
     * 是否匿名访问
     */
    private Boolean isAnonymous;
}
