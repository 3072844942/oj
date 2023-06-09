package org.oj.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.EntityStateEnum;

/**
 * 相册
 *
 * @author bin
 * @date 2021/08/04
 */
@Data
@Builder
@NoArgsConstructor
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
    private String content;

    /**
     * 相册封面
     */
    private String cover;

    /**
     * 是否删除
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

    public static void check(PhotoAlbumDTO albumDTO) {

    }
}