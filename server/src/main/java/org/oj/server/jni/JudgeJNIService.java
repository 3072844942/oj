package org.oj.server.jni;

import org.oj.server.entity.Record;
import org.oj.server.entity.Result;
import org.oj.server.entity.Task;
import org.oj.server.util.LibLoader;

import java.net.URL;

/**
 * 链接本地c++代码的判题服务
 *
 * @author march
 * @since 2023/7/3 上午8:40
 */
public class JudgeJNIService {
    static {
        /* 打包jar时，如果本地有也可以使用这个。地址为/tmp/lib/... */
        LibLoader.loadLib("jni/libJudgeCore.so");

        /* 本地运行，这里对应resource */
//        URL url = JudgeJNIService.class.getClassLoader().getResource("jni/libJudgeCore.so");
//        System.load(url.getPath());
    }

    public static native String hello();

    public static native Result judge(Task o);
}
