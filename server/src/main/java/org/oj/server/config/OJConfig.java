package org.oj.server.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * oj配置
 *
 * @author march
 * @since 2023/6/27 下午3:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class OJConfig {
    @Value("${oj.upload.base}")
    private String base;

    @Value("${oj.upload.url}")
    private String urlBase;
}
