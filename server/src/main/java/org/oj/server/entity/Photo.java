package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 照片
 *
 * @author bin
 * @date 2021/08/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("photo")
public class Photo {

    /**
     * 照片id
     */
    @Id
    private String id;

    /**
     * 相册id
     */
    private String albumId;

    /**
     * 照片名
     */
    private String name;

    /**
     * 照片描述
     */
    private String desc;

    /**
     * 照片地址
     */
    private String url;

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