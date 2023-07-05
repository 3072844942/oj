package org.oj.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.PhotoAlbumDTO;
import org.oj.server.service.PhotoAlbumService;
import org.springframework.web.bind.annotation.*;

/**
 * @author march
 * @since 2023/7/4 上午9:51
 */
@RestController
@RequestMapping("album")
@Tag(name = "相册接口")
public class PhotoAlbumController extends BaseController {
    private final PhotoAlbumService albumService;

    public PhotoAlbumController(PhotoAlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("list")
    public Object list(ConditionDTO conditionDTO) {
        return ok(albumService.list(conditionDTO));
    }

    @PatchMapping("update")
    public Object updateOne(@RequestBody PhotoAlbumDTO albumDTO) {
        return ok(albumService.updateOne(albumDTO));
    }

    @PutMapping("add")
    public Object insertOne(@RequestBody PhotoAlbumDTO albumDTO) {
        return ok(albumService.insertOne(albumDTO));
    }

    @DeleteMapping("delete/{albumId}")
    public Object deleteOne(@PathVariable String albumId) {
        albumService.deleteOne(albumId);
        return ok();
    }
}
