package org.oj.server.util;

/**
 * @author march
 * @since 2023/6/2 上午9:11
 */
public class StringUtils {
    /**
     * 判断字符串是否为空
     * @param str
     * @return 是空字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("");
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return 不是空字符串
     */
    public static boolean isPresent(String str) {
        return str != null && !str.trim().equals("");
    }
}
