package org.oj.server.dto;

import lombok.*;

/**
 * 相册
 *
 * @author bin
 * @date 2021/08/04
 */
@Getter
@AllArgsConstructor
public class PhotoAlbumDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 相册名
     */
    private String title;

    /**
     * 相册描述
     */
    private String desc;

    /**
     * 相册封面
     */
    private String cover;

    /**
     * 是否删除
     */
    private Integer state;

    /**
     * 权限
     */
    private Integer permission;
}