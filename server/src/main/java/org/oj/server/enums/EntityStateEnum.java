package org.oj.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实体类状态枚举
 */
@Getter
@AllArgsConstructor
public enum EntityStateEnum {
    /**
     * 删除
     */
    DELETE(0, "删除"),
    /**
     * 草稿
     */
    DRAFT(1, "草稿"),
    /**
     * 审核
     */
    REVIEW(2, "审核"),
    /**
     * 公开
     */
    PUBLIC(3, "公开");

    /**
     * 编号
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String desc;

    public static EntityStateEnum valueOf(Integer state) {
        EntityStateEnum[] values = EntityStateEnum.values();
        for (EntityStateEnum i : values) {
            if (i.getCode().equals(state)) {
                return i;
            }
        }
        return EntityStateEnum.DRAFT;
    }
}
