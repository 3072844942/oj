package org.oj.server.util;

import org.oj.server.constant.HtmlConst;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 检测邮箱是否合法
     *
     * @param email 用户名
     * @return 合法状态
     */
    public static boolean checkEmail(String email) {
        String rule = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(rule);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }


    /**
     * 生成6位随机验证码
     *
     * @return 验证码
     */
    public static String getRandomCode(int length) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 截取关键字周围片段
     * @param content 内容
     * @param keywords 关键字
     * @return 长度不超过200
     */
    public static String subKeywords(String content, String keywords) {
        int index = content.indexOf(keywords);
        if (index != -1) {
            // 获取关键词前面的文字
            int preIndex = index > 25 ? index - 25 : 0;
            String preText = content.substring(preIndex, index);
            // 获取关键词到后面的文字
            int last = index + keywords.length();
            int postLength = content.length() - last;
            int postIndex = postLength > 175 ? last + 175 : last + postLength;
            String postText = content.substring(index, postIndex);
            // 文章内容高亮
            return  (preText + postText).replaceAll(keywords, HtmlConst.PRE_TAG + keywords + HtmlConst.POST_TAG);
        } else {
            return content;
        }
    }


    /**
     * 检查字符串是否符合长度
     * @param str 字符串
     * @param l 最小长度
     * @param r 最大长度
     * @return 是否符合
     */
    public static boolean isSpecifiedLength(String str, int l, int r) {
        return str.length() >= l && str.length() <= r;
    }
}
