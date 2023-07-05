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
    DELETE(1, "删除"),
    /**
     * 草稿
     */
    DRAFT(2, "草稿"),
    /**
     * 审核
     */
    REVIEW(3, "审核"),
    /**
     * 公开
     */
    PUBLIC(4, "公开");

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
