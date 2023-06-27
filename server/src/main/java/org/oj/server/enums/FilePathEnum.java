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
    IMAGE("image/", "图片路径"),
    /**
     * 音频路径
     */
    AUDIO("voice/", "音频路径"),

    /**
     * 视频路径
     */
    VIDEO("video/", "视频路径"),
    /**
     * 照片路径
     */
    PHOTO("photos/","相册路径"),
    /**
     * 题目路径
     */
    RECORD("record/", "题目数据"),
    /**
     * 配置图片路径
     */
    CONFIG("config/","配置图片路径"),
    JUDGE("judge/", "评测路径"),
    EXCEL("excel/", "表格路径");

    /**
     * 路径
     */
    private final String path;

    /**
     * 描述
     */
    private final String desc;

}
