package org.oj.server.dto;

import lombok.*;
import org.oj.server.enums.EntityStateEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 照片
 *
 * @author bin
 * @date 2021/08/04
 */
@Getter
@AllArgsConstructor
public class PhotoDTO {

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
}