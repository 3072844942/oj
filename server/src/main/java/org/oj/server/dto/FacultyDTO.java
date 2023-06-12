package org.oj.server.dto;

import lombok.*;
import org.oj.server.entity.Faculty;
import org.oj.server.exception.WarnException;
import org.oj.server.util.BeanCopyUtils;
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
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDTO {
    /**
     * id
     */
    private String id;

    /**
     * 学院名称
     */
    private String title;

    /**
     * 学院描述
     */
    private String desc;

    public static WarnException check(FacultyDTO facultyDTO) {
        return null;
    }

    public static FacultyDTO of(Faculty faculty) {
        return BeanCopyUtils.copyObject(faculty, FacultyDTO.class);
    }
}
