package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 相册
 *
 * @author bin
 * @date 2021/08/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(MongoConst.PHOTO_ALBUM)
public class PhotoAlbum {

    /**
     * 主键
     */
    @Id
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
    private EntityStateEnum state;

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