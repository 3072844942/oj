package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.entity.Photo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author march
 * @since 2023/7/4 上午8:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoVO {

    /**
     * 照片id
     */
    private String id;

    /**
     * 相册id
     */
    private String albumId;

    /**
     * 照片名
     */
    private String title;

    /**
     * 照片描述
     */
    private String content;

    /**
     * 照片地址
     */
    private String url;

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

    public static PhotoVO of(Photo photo) {
        PhotoVO photoVO = BeanCopyUtils.copyObject(photo, PhotoVO.class);
        photoVO.setState(photo.getState().getCode());
        return photoVO;
    }
}
