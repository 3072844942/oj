package org.oj.server.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 分类
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Getter
@AllArgsConstructor
public class CategoryDTO {

    /**
     * id
     */
    private String id;

    /**
     * 分类名
     */
    private String title;
}
