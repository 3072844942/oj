package org.oj.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oj.server.enums.JudgeStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.jni.JudgeJNIService;
import org.oj.server.util.FileUtils;
import org.oj.server.util.LanguageUtil;
import org.oj.server.util.StringUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author march
 * @since 2023/6/12 下午4:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Callable<Record> {
    /**
     * 代码地址
     */
    private String code;

    /**
     * 语言
     */
    private Integer language;

    /**
     * 标准输入文件地址
     */
    private Problem problem;

    /**
     * 用户输出文件夹地址
     */
    private String out;

    @Override
    public Record call() {
//        return judge();
        return JudgeJNIService.judge(this);
    }

    /**
     * 不能对程序的运行进行限制
     */
    @Deprecated
    public Record judge() {
        Record record = Record.builder()
                .build();
        if (LanguageUtil.isCPP(language)) { // c++,
            // 编辑为可运行文件
            try {
                code = LanguageUtil.compile(code, language);
            } catch (ErrorException e) {
                // 发生编辑错误
                record.setResult(JudgeStateEnum.FLOAT_ERROR);
                record.setDesc(e.getMessage());
                return record;
            } catch (IOException e) {
                // 发生系统错误
                record.setResult(JudgeStateEnum.UNKNOWN_ERROR);
                record.setDesc(e.getMessage());
                return record;
            }
        }

        String cmd = LanguageUtil.cmdPrefix(language) + code;
        for (int i = 1; ; i++) {
            String inPath = problem.getAddress() + i + ".in";
            // 文件不存在则退出
            if (!FileUtils.exists(inPath)) {
                break;
            }

            try {
                String input = FileUtils.read(inPath);

                // 使用 Runtime.exec() 执行 ulimit 命令设置资源限制
                Runtime.getRuntime().exec("ulimit -m " + problem.getMemoryRequire()); // 设置最大内存限制
                Runtime.getRuntime().exec("ulimit -t " + problem.getTimeRequire() / 1000.0d);      // 设置 CPU 时间限制

                // 创建进程构建器
                ProcessBuilder processBuilder = new ProcessBuilder(cmd);
                // 启动进程
                Process process = processBuilder.start();
                // 写入输入
                OutputStream outputStream = process.getOutputStream();
                outputStream.write(input.getBytes());
                outputStream.flush();
                outputStream.close();

                boolean finished = process.waitFor(problem.getTimeRequire(), TimeUnit.MILLISECONDS);
                record.setTimeCost(ManagementFactory.getRuntimeMXBean().getUptime());
                record.setMemoryCost(ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / (1024 * 1024));
                if (!finished) {
                    // 超时
                    record.setResult(JudgeStateEnum.TIME_LIMIT_EXCEEDED);
                    return record;
                } else if (record.getMemoryCost() >= problem.getMemoryRequire()) {
                    // 内存超限
                    record.setResult(JudgeStateEnum.MEMORY_LIMIT_EXCEED);
                    return record;
                }

                // 程序输出
                StringBuilder output = new StringBuilder();
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }

                // 特判
                if (problem.getIsSpecial()) {
                    // 写入文件
                    FileUtils.write(out + i + ".out", output.toString());

                    // 传入两个文件
                    ProcessBuilder p = new ProcessBuilder(problem.getSpecialAddress(), inPath, out + i + ".out");
                    Process start = p.start();

                    // 检查退出码
                    int exitCode = start.waitFor();
                    if (exitCode == 0) {
                        record.setResult(JudgeStateEnum.ACCEPTED);
                    } else {
                        record.setResult(JudgeStateEnum.WRONG_ANSWER);
                    }
                } else {
                    String stdOutput = FileUtils.read(problem.getAddress() + i + ".out");
                    // 比对
                    if (!StringUtils.compile(output.toString(), stdOutput)) {
                        record.setResult(JudgeStateEnum.WRONG_ANSWER);
                    } else {
                        record.setResult(JudgeStateEnum.ACCEPTED);
                    }
                }
            } catch (IOException | InterruptedException e) {
                record.setResult(JudgeStateEnum.RUNTIME_ERROR);
            }
        }
        return record;
    }
}
