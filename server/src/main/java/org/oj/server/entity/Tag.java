package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.dto.TagDTO;
import org.oj.server.util.BeanCopyUtils;
import org.oj.server.util.SensitiveUtils;
import org.springframework.data.annotation.CreatedDate;
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
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConst.TAG)
public class Tag {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 标签
     */
    private String title;

    /**
     * 创建时间
     */
    @CreatedDate
    private Long createTime;

    public static Tag of(TagDTO tagDTO) {
        Tag tag = BeanCopyUtils.copyObject(tagDTO, Tag.class);
        tag.setTitle(SensitiveUtils.filter(tagDTO.getTitle()));
        return tag;
    }
}