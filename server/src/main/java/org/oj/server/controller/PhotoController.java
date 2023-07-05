package org.oj.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.dto.PhotoDTO;
import org.oj.server.service.PhotoAlbumService;
import org.oj.server.service.PhotoService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理已经上传的图片
 * @author march
 * @since 2023/5/31 下午3:19
 */
@RestController
@RequestMapping("photo")
@Tag(name = "图片接口")
public class PhotoController extends BaseController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("{albumId}")
    public Object list(@PathVariable String albumId) {
        return ok(photoService.list(albumId));
    }

    @PatchMapping("update")
    public Object updateOne(@RequestBody PhotoDTO photoDTO) {
        return ok(photoService.updateOne(photoDTO));
    }

    @PutMapping("add")
    public Object insertOne(@RequestBody PhotoDTO photoDTO) {
        return ok(photoService.insertOne(photoDTO));
    }

    @DeleteMapping("delete/{photoId}")
    public Object deleteOne(@PathVariable String photoId) {
        photoService.deleteOne(photoId);
        return ok();
    }
}
