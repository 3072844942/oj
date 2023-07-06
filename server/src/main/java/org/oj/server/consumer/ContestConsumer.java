package org.oj.server.consumer;

import com.alibaba.fastjson.JSON;
import org.oj.server.constant.MQPrefixConst;
import org.oj.server.constant.RedisPrefixConst;
import org.oj.server.dao.ContestRepository;
import org.oj.server.dao.ProblemRepository;
import org.oj.server.entity.Contest;
import org.oj.server.entity.RankInfo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.service.RedisService;
import org.oj.server.vo.ContestInfoVO;
import org.oj.server.vo.ProblemVO;
import org.oj.server.vo.RankInfoVO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author march
 * @since 2023/7/6 上午10:36
 */
@Component
@RabbitListener(queues = MQPrefixConst.CONTEXT_DEAD_QUEUE)
public class ContestConsumer {
    private final ContestRepository contestRepository;
    private final RedisService redisService;
    private final ProblemRepository problemRepository;

    public ContestConsumer(ContestRepository contestRepository, RedisService redisService, ProblemRepository problemRepository) {
        this.contestRepository = contestRepository;
        this.redisService = redisService;
        this.problemRepository = problemRepository;
    }

    @RabbitHandler
    public void process(byte[] data) {
        String contestId = JSON.parseObject(new String(data), String.class);
        Optional<Contest> byId = contestRepository.findById(contestId);
        if (byId.isEmpty()) {
            return;
        }
        Contest contest = byId.get();
        // 当前时间 s
        long currentSecond = System.currentTimeMillis() / 1000;
        // 如果是比赛开始前3分钟， 误差小于10s
        if (Math.abs(currentSecond - contest.getStartTime()) < 5 * 60
                && !contest.getState().equals(EntityStateEnum.DELETE)) {
            // 设置头表示比赛开始
            long timeout = contest.getEndTime() - contest.getStartTime() + 10 * 60;
            ContestInfoVO contestInfoVO = ContestInfoVO.of(contest);
            redisService.set(RedisPrefixConst.CONTEXT + contestId, contestInfoVO, timeout);
            Map<String, ProblemVO> problemVOMap =
                    problemRepository.findAllById(contest.getProblemIds()).stream()
                            .map(ProblemVO::of)
                            .collect(Collectors.toMap(ProblemVO::getId, a -> a, (k1, k2) -> k1));

            redisService.hSetAll(RedisPrefixConst.CONTEXT_PROBLEM + contestId, problemVOMap);
        } else if (Math.abs(currentSecond - contest.getEndTime()) < 5 * 60) {
            Map map = redisService.hGetAll(RedisPrefixConst.CONTEXT_RANK_MAP + contestId);
            List<RankInfo> list = map.values().stream().map(i -> {
                RankInfoVO infoVO = (RankInfoVO) i;
                return RankInfo.of(infoVO);
            }).toList();

            contest.setRank(list.stream().collect(Collectors.toMap(RankInfo::getUserId, a -> a, (k1, k2) -> k1)));
            contestRepository.save(contest);
        }
    }
}
