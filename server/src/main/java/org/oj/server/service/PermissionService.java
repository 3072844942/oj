package org.oj.server.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.oj.server.dao.PermissionRepository;
import org.oj.server.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;

/**
 * @author march
 * @since 2023/5/31 下午3:13
 */
@Log4j2
@Service
public class PermissionService {
    private final List<String> white = Arrays.asList("/user/login", "/user/register", "/user/send");
    private final WebApplicationContext applicationContext;
    private final PermissionRepository permissionRepository;

    public PermissionService(WebApplicationContext applicationContext, PermissionRepository permissionRepository) {
        this.applicationContext = applicationContext;
        this.permissionRepository = permissionRepository;
    }

    /**
     * 加载所有信息
     */
    public void init() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);

        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        // 遍历
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();

            // 请求路径
            PathPatternsRequestCondition requestCondition = info.getPathPatternsCondition();

            List<String> collect = new ArrayList<>(1);
            if (requestCondition != null) {
                Set<PathPattern> patterns = requestCondition.getPatterns();
                collect = patterns
                        .stream()
                        .map(PathPattern::getPatternString)
                        .toList();
            }

            // 请求方法
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();

            String uri = collect.get(0);
            List<RequestMethod> list = methodsCondition.getMethods().stream().toList();
            if (list.size() == 0) continue;
            String method = list.get(0).toString();

            // 初始化权限
            log.info("path: " + uri + "\t methods: " + methodsCondition.getMethods());

            // 跳过swagger文档
            if (uri.contains("/v3") || uri.contains("swagger")) continue;

            // 前缀
            String profileUri = "/" + uri.split("/")[1];
            Permission permission = new Permission(
                    uri,
                    uri,
                    uri,
                    method,
                    profileUri,
                    method.equals("GET") || white.contains(uri),
                    0L, 0L
            );
            permissionRepository.save(permission);

            // 如果前缀不存在
            if (!permissionRepository.existsByUrl(profileUri)) {
                Permission p = new Permission(
                        profileUri,
                        profileUri,
                        profileUri,
                        "GET",
                        null,
                        true,
                        0L, 0L
                );
                permissionRepository.save(p);
            }
        }
    }
}
