package org.oj.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Document("tag")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 标签
     */
    private String name;

    /**
     * 创建时间
     */
    private Long createTime;
}