package org.oj.server.util;

import org.oj.server.enums.JudgeLanguageEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author march
 * @since 2023/6/12 下午5:00
 */
public class LanguageUtil {
    /**
     * 获取文件名后缀
     * @param language 语言类型
     * @return 后缀
     */
    public static String fileSuffix(Integer language) {
        if (language.equals(JudgeLanguageEnum.CPP.getCode())) return ".cpp";
        if (language.equals(JudgeLanguageEnum.JAVA.getCode())) return ".java";
        if (language.equals(JudgeLanguageEnum.PYTHON3.getCode())) return ".py";

        throw new WarnException(StatusCodeEnum.FAIL);
    }

    public static boolean isCPP(Integer language) {
        return language.equals(0);
    }

    public static String compile(String path, Integer language) throws IOException {
        if (language.equals(0)) { // c ++
            ProcessBuilder process = new ProcessBuilder("/usr/bin/g++",
                    path,
                    "-o", path + "/app");

            Process p = process.start();

            StringBuilder answer = new StringBuilder();
            InputStream errorStream = p.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            if (StringUtils.isPresent(answer.toString())) {
                throw new ErrorException(StatusCodeEnum.COMPILE_ERROR.getCode(), answer.toString());
            }
            return path + "/app";
        }

        throw new ErrorException(StatusCodeEnum.LANGUAGE_NOT_SUPPORT);
    }

    /**
     * 程序前缀
     * @param language
     * @return
     */
    public static String cmdPrefix(Integer language) {
        if (language.equals(JudgeLanguageEnum.CPP.getCode())) return "";
        if (language.equals(JudgeLanguageEnum.JAVA.getCode())) return "java";
        if (language.equals(JudgeLanguageEnum.PYTHON3.getCode())) return "python3";

        throw new WarnException(StatusCodeEnum.FAIL);
    }
}
