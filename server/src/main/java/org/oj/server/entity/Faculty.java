package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.constant.MongoConst;
import org.oj.server.dto.FacultyDTO;
import org.oj.server.util.BeanCopyUtils;
import org.springframework.data.annotation.CreatedDate;
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
@Document(MongoConst.FACULTY)
public class Faculty {
    /**`
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
    private String content;

    /**
     * 创建时间
     */
    @CreatedDate
    private Long createTime;

    public static Faculty of(FacultyDTO facultyDTO) {
        return BeanCopyUtils.copyObject(facultyDTO, Faculty.class);
    }
}
