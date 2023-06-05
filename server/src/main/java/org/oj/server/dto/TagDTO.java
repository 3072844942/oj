package org.oj.server.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 文章标签
 * 标签
 *
 * @author xiaojie
 * @date 2021/07/29
 * @since 2020-05-18
 */
@Getter
@AllArgsConstructor
public class TagDTO {

    /**
     * id
     */
    private String id;

    /**
     * 标签
     */
    private String name;
}