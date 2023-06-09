package org.oj.server.service;

import jakarta.annotation.PostConstruct;
import org.oj.server.dao.RoleRepository;
import org.oj.server.entity.Permission;
import org.oj.server.entity.Role;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author march
 * @since 2023/5/31 下午3:14
 */
@Service
// 保证permission先写入数据库
@DependsOn("permissionService")
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;


    @PostConstruct
    private void init() {
        // todo menuList
        Collection<Permission> values = PermissionService.permissionMap.values();
        Map<String, PermissionEnum> all = new HashMap<>();
        values.forEach(i -> {
            all.put(i.getId(), PermissionEnum.WRITE);
        });

        Role root = new Role(
                "root",
                "根用户",
                EntityStateEnum.PUBLIC,
                null,
                all,
                0L, 0L
        );
        if (!roleRepository.existsById(root.getId()))
            roleRepository.insert(root);

        Role admin = new Role(
                "admin",
                "管理员",
                EntityStateEnum.PUBLIC,
                null,
                all,
                0L, 0L
        );
        if (!roleRepository.existsById(admin.getId()))
            roleRepository.insert(admin);


        Map<String, PermissionEnum> map = new HashMap<>();
        values.forEach(i -> {
            String method = i.getMethod();
            if (method.equals("GET")) {
                map.put(i.getId(), PermissionEnum.WRITE);
            } else {
                map.put(i.getId(), PermissionEnum.NONE);
            }
        });
        Role user = new Role(
                "user",
                "普通用户",
                EntityStateEnum.PUBLIC,
                null,
                map,
                0L, 0L
        );
        if (!roleRepository.existsById(user.getId()))
            roleRepository.insert(user);
    }
}
