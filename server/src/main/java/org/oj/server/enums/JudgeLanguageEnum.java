package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author march
 * @since 2023/7/3 下午4:08
 */
@Getter
@AllArgsConstructor
public enum JudgeLanguageEnum {

    CPP(1, "CPP"),
    JAVA(2, "JAVA"),
    PYTHON3(3, "PYTHON3");

    private Integer code;

    private String desc;
}
