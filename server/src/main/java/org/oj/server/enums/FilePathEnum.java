package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件路径枚举
 *
 * @author bin
 * @date 2021/08/04
 */
@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 文章图片路径
     */
    IMAGE(1, "image/", "图片"),
    /**
     * 音频路径
     */
    AUDIO(2, "voice/", "音频"),

    /**
     * 视频路径
     */
    VIDEO(3, "video/", "视频"),
    /**
     * 照片路径
     */
    PHOTO(4, "photo/","相册"),
    /**
     * 题目路径
     */
    RECORD(5, "record/", "题目"),
    /**
     * 配置图片路径
     */
    CONFIG(6, "config/","配置图片"),

    JUDGE(7, "judge/", "评测"),

    EXCEL(8, "excel/", "表格");

    private final Integer code;

    /**
     * 路径
     */
    private final String path;

    /**
     * 描述
     */
    private final String desc;

    public static FilePathEnum valueOf(Integer pathEnum) {
        FilePathEnum[] values = FilePathEnum.values();
        for (FilePathEnum i : values) {
            if (i.getCode().equals(pathEnum)) {
                return i;
            }
        }
        return IMAGE;
    }
}
