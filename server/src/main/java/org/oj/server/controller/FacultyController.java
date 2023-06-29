package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.FacultyDTO;
import org.oj.server.service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:18
 */
@RestController
@RequestMapping("faculty")
@Tag(name = "学院接口")
public class FacultyController extends BaseController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Operation(summary = "查找学院列表")
    @GetMapping("list")
    public Object find(ConditionDTO conditionDTO) {
        return facultyService.find(conditionDTO);
    }

    @Operation(summary = "更新学院")
    @PatchMapping("update")
    public Object updateOne(@RequestBody FacultyDTO facultyDTO) {
        return ok(facultyService.updateOne(facultyDTO));
    }

    @Operation(summary = "插入学院")
    @PutMapping("add")
    public Object insertOne(@RequestBody FacultyDTO facultyDTO) {
        return ok(facultyService.insertOne(facultyDTO));
    }

    @Operation(summary = "批量删除学院")
    @DeleteMapping("delete/list")
    public Object delete(@RequestBody List<String> ids) {
        facultyService.delete(ids);
        return ok();
    }

    @Operation(summary = "删除学院")
    @DeleteMapping("delete/{facultyId}")
    public Object deleteOne(@PathVariable String facultyId) {
        facultyService.deleteOne(facultyId);
        return ok();
    }
}
