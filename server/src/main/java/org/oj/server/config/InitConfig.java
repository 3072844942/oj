package org.oj.server.config;

import org.oj.server.service.PermissionService;
import org.oj.server.service.PhotoAlbumService;
import org.oj.server.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化
 * Q： 为什么不用@PostConstruct
 * A: 为了保证运行顺序， 希望在运行完Mongo监听之后再初始化
 *    而@PostConstruct不能保证顺序
 *
 * @author march
 * @since 2023/6/27 上午11:09
 */
@Component
public class InitConfig implements CommandLineRunner {
    private final RoleService roleService;
    private final PermissionService permissionService;

    public InitConfig(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    public void run(String... args) {
        permissionService.init();
        roleService.init();
    }
}
