package org.oj.server.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 菜单
 *
 * @author bin
 * @date 2021/08/01
 */
@Getter
@AllArgsConstructor
public class MenuDTO {

    /**
     * id
     */
    private String id;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * icon
     */
    private String icon;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 是否隐藏
     */
    private Integer state;
}

