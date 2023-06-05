package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 学院
 *
 * @author march
 * @since 2023/5/31 上午10:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("faculty")
public class Faculty {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 学院名称
     */
    private String title;

    /**
     * 学院描述
     */
    private String desc;

    /**
     * 创建时间
     */
    private Long createTime;
}
