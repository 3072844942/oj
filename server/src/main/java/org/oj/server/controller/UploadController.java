package org.oj.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.oj.server.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 上传  图片/音频/语音/压缩包
 * @author march
 * @since 2023/5/31 下午3:20
 */
@RestController
@RequestMapping("upload")
@Tag(name = "上传接口")
public class UploadController extends BaseController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Operation(summary = "上传图片")
    @PutMapping("image")
    public Object uploadImage(@RequestBody MultipartFile file) {
        return ok(uploadService.uploadImage(file));
    }

    @Operation(summary = "上传视频")
    @PutMapping("video")
    public Object uploadVideo(@RequestBody MultipartFile file) {
        return ok(uploadService.uploadVideo(file));
    }

    @Operation(summary = "上传音频")
    @PutMapping("audio")
    public Object uploadAudio(@RequestBody MultipartFile file) {
        return ok(uploadService.uploadAudio(file));
    }

    @Operation(summary = "上传题目测试数据")
    @PutMapping("record")
    public Object uploadRecord(@RequestBody MultipartFile file) {
        return ok(uploadService.uploadRecord(file));
    }
}
