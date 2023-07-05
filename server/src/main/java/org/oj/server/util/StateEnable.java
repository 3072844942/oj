package org.oj.server.util;

import org.oj.server.enums.EntityStateEnum;

/**
 * @author march
 * @since 2023/7/5 上午8:50
 */
public interface StateEnable {
    void setState(EntityStateEnum state);
    EntityStateEnum getState();
}
