package org.oj.server.config;

import org.oj.server.service.PermissionService;
import org.oj.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化
 *
 * @author march
 * @since 2023/6/27 上午11:09
 */
@Component
public class InitConfig implements CommandLineRunner {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public void run(String... args) {
        permissionService.init();
        roleService.init();
    }
}
