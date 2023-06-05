package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 分类
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("category")
public class Category {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 分类名
     */
    private String title;

    /**
     * 创建时间
     */
    private Long createTime;
}
