package org.oj.server.util;

import org.oj.server.dto.PoolInfoDTO;
import org.oj.server.entity.Task;
import org.oj.server.entity.Record;
import org.oj.server.vo.PoolInfoVO;

import java.util.concurrent.*;

/**
 * @author march
 * @since 2023/6/12 下午4:14
 */
public class JudgePool {
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(10,
            100,
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(500),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());


    public static Future<Record> execute(Task callable) {
        return pool.submit(callable);
    }

    public static PoolInfoVO getInfo() {
        return PoolInfoVO.builder()
                .coreSize(pool.getCorePoolSize())
                .maxSize(pool.getMaximumPoolSize())
                .keepAliveTime(pool.getKeepAliveTime(TimeUnit.MILLISECONDS))
                .unit(valueOf(TimeUnit.MILLISECONDS))
                .workSize(pool.getActiveCount())
                .waitSize(pool.getQueue().size())
                .build();
    }


    public static int valueOf(TimeUnit unit) {
        if (unit.equals(TimeUnit.NANOSECONDS)) return 0;
        if (unit.equals(TimeUnit.MICROSECONDS)) return 1;
        if (unit.equals(TimeUnit.MILLISECONDS)) return 2;
        if (unit.equals(TimeUnit.SECONDS)) return 3;
        if (unit.equals(TimeUnit.MINUTES)) return 4;
        if (unit.equals(TimeUnit.HOURS)) return 5;
        if (unit.equals(TimeUnit.DAYS)) return 6;
        return 0;
    }

    public static TimeUnit valueOf(Integer state) {
        if (state.equals(0)) return TimeUnit.NANOSECONDS;
        if (state.equals(1)) return TimeUnit.MILLISECONDS;
        if (state.equals(2)) return TimeUnit.MILLISECONDS;
        if (state.equals(3)) return TimeUnit.SECONDS;
        if (state.equals(4)) return TimeUnit.SECONDS;
        if (state.equals(5)) return TimeUnit.HOURS;
        if (state.equals(6)) return TimeUnit.DAYS;
        return TimeUnit.NANOSECONDS;
    }

    public static PoolInfoVO update(PoolInfoDTO poolInfoDTO) {
        pool.setCorePoolSize(poolInfoDTO.getCorePoolSize());
        pool.setMaximumPoolSize(poolInfoDTO.getMaxPoolSize());
        pool.setKeepAliveTime(poolInfoDTO.getKeepAliveTime(), valueOf(poolInfoDTO.getUnit()));

        return getInfo();
    }
}
