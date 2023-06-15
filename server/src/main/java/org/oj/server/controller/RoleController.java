package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.RoleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * 角色维护， 还要维护角色和菜单权限的关系
 * @author march
 * @since 2023/5/31 下午3:19
 */
@RestController
@RequestMapping("role")
@Tag(name = "角色接口")
public class RoleController extends BaseController{
    @Autowired
    private RoleService roleService;

    @Operation(summary = "根据id查找角色")
    @GetMapping("{roleId}")
    public Object findOne(@PathVariable String roleId) {
        return ok(roleService.findOne(roleId));
    }

    @Operation(summary = "查找角色列表")
    @GetMapping("list")
    public Object list(ConditionDTO conditionDTO) {
        return ok(roleService.find(conditionDTO));
    }

    @Operation(summary = "更新角色")
    @PatchMapping("update")
    public Object updateOne(@RequestBody RoleDTO roleDTO) {
        return ok(roleService.updateOne(roleDTO));
    }

    @Operation(summary = "添加角色")
    @PutMapping("add")
    public Object insertOne(@RequestBody RoleDTO roleDTO) {
        return ok(roleService.insertOne(roleDTO));
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("delete/{roleId}")
    public Object deleteOne(@PathVariable String roleId) {
        roleService.deleteOne(roleId);
        return ok();
    }
}
