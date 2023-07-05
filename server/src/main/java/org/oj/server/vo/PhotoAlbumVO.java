package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.PhotoAlbum;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;

/**
 * @author march
 * @since 2023/7/4 上午9:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoAlbumVO {

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

    public static PhotoAlbumVO of(PhotoAlbum photoAlbum) {
        PhotoAlbumVO photoAlbumVO = BeanCopyUtils.copyObject(photoAlbum, PhotoAlbumVO.class);
        photoAlbumVO.setState(photoAlbum.getState().getCode());
        return photoAlbumVO;
    }
}
