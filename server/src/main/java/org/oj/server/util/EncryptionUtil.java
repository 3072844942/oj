package org.oj.server.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 加密工具
 *
 * @author march
 * @since 2023/6/2 下午5:37
 */
public class EncryptionUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 加密
     *
     * @param str 明文
     * @return 密文
     */
    public static String encode(String str) {
        return passwordEncoder.encode(str);
    }

    /**
     * 匹配
     *
     * @param str  明文
     * @param pass 密文
     * @return 是否匹配
     */
    public static boolean match(String str, String pass) {
        return passwordEncoder.matches(str, pass);
    }
}
