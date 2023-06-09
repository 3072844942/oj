package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 资源
 *
 * @author bin
 * @date 2021/08/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("permission")
public class Permission {
    /**
     * 权限id
     */
    @Id
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
    private String parentId;

    /**
     * 是否匿名访问
     */
    private Boolean isAnonymous;

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
