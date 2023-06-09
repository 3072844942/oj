package org.oj.server.service;

import org.oj.server.config.OJConfig;
import org.oj.server.constant.MongoConst;
import org.oj.server.dao.RecordRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.JudgeDTO;
import org.oj.server.dto.PoolInfoDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Contest;
import org.oj.server.entity.Problem;
import org.oj.server.entity.Record;
import org.oj.server.entity.Task;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.FilePathEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.JudgePool;
import org.oj.server.util.LanguageUtil;
import org.oj.server.util.QueryUtils;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.PageVO;
import org.oj.server.vo.PoolInfoVO;
import org.oj.server.vo.RecordVO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author march
 * @since 2023/6/12 下午3:59
 */
@Service
public class JudgeService {
    private final ProblemService problemService;
    private final ContestService contestService;
    private final UploadService uploadService;
    private final MongoTemplate mongoTemplate;
    private final RecordRepository recordRepository;
    private final OJConfig ojConfig;

    public JudgeService(ProblemService problemService, ContestService contestService, UploadService uploadService, MongoTemplate mongoTemplate, RecordRepository recordRepository, OJConfig ojConfig) {
        this.problemService = problemService;
        this.contestService = contestService;
        this.uploadService = uploadService;
        this.mongoTemplate = mongoTemplate;
        this.recordRepository = recordRepository;
        this.ojConfig = ojConfig;
    }

    @Transactional
    public RecordVO judge(JudgeDTO judgeDTO) {
        // 未登陆
        if (Request.user.get() == null) {
            throw new WarnException(StatusCodeEnum.NOT_LOGIN);
        }

        // 写入本地
        String path = uploadService.upload(judgeDTO.getCode(), judgeDTO.getLanguage());

        Problem problem = problemService.findById(judgeDTO.getProblemId());
        Contest contest = null;
        if (StringUtils.isPresent(judgeDTO.getContestId())) {
            contest = contestService.findById(judgeDTO.getContestId());

            // 不是比赛中的题目
            if (!contest.getProblemIds().contains(judgeDTO.getProblemId())) {
                throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
            }
            long currentTime = System.currentTimeMillis() / 1000;
            // 未开始 || 已结束
            if (contest.getStartTime() > currentTime || contest.getEndTime() < currentTime) {
                throw new ErrorException(StatusCodeEnum.NOT_RUNNING);
            }
        }

        // 生成任务
        Task task = Task.builder()
                .code(path)
                .language(judgeDTO.getLanguage())
                .problem(problem)
                .out(ojConfig.getBase() + FilePathEnum.JUDGE.getPath() + System.currentTimeMillis() + "/")
                .build();

        // 等待运行
        Future<Record> execute = JudgePool.execute(task);
        Record record;
        try {
            record = execute.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ErrorException(StatusCodeEnum.JUDGE_ERROR);
        }

        // 设置额外信息
        record.setUserId(Request.user.get().getId());
        record.setProblemId(problem.getId());
        record.setCode(judgeDTO.getCode());
        record.setState(EntityStateEnum.DRAFT);
        if (StringUtils.isPresent(judgeDTO.getContestId())) {
            // 这里没有问题， 到这里时一定会找到contest
            record.setContestId(contest.getId());
            // 提交缓存
            contestService.save(record);
        }

        Record insert = recordRepository.insert(record);

        return RecordVO.of(insert);
    }

    public String debug(JudgeDTO judgeDTO) {
        // 写入本地
        String path = uploadService.upload(judgeDTO.getCode(), judgeDTO.getLanguage());

        if (LanguageUtil.isCPP(judgeDTO.getLanguage())) { // c++,
            // 编辑为可运行文件
            try {
                path = LanguageUtil.compile(path, judgeDTO.getLanguage());
            } catch (IOException e) {
                throw new ErrorException(StatusCodeEnum.FAIL.getCode(), e.getMessage());
            }
        }

        String cmd = LanguageUtil.cmdPrefix(judgeDTO.getLanguage()) + path;
        try {
            // 使用 Runtime.exec() 执行 ulimit 命令设置资源限制
            Runtime.getRuntime().exec("ulimit -m 10000000"); // 设置最大内存限制为 10MB
            Runtime.getRuntime().exec("ulimit -t 10");      // 设置 CPU 时间限制为 10 秒

            // 创建进程构建器
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            // 启动进程
            Process process = processBuilder.start();
            // 写入输入
            OutputStream outputStream = process.getOutputStream();
            outputStream.write(judgeDTO.getInput().getBytes());
            outputStream.flush();
            outputStream.close();

            boolean finished = process.waitFor(10, TimeUnit.SECONDS);
            if (!finished) {
                throw new ErrorException(StatusCodeEnum.FAIL);
            }
            // 程序输出
            StringBuilder output = new StringBuilder();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            return output.toString();
        } catch (IOException | InterruptedException e) {
            throw new ErrorException(StatusCodeEnum.FAIL.getCode(), e.getMessage());
        }
    }

    public String test(JudgeDTO judgeDTO) {
        String path = uploadService.upload(judgeDTO.getCode(), judgeDTO.getLanguage());

        if (!LanguageUtil.isCPP(judgeDTO.getLanguage())) {
            throw new WarnException(StatusCodeEnum.LANGUAGE_NOT_SUPPORT);
        }

        // 只做编译校验
        try {
            return LanguageUtil.compile(path, judgeDTO.getLanguage());
        } catch (IOException e) {
            throw new ErrorException(StatusCodeEnum.COMPILE_ERROR.getCode(), e.getMessage());
        }
    }

    public PoolInfoVO getPool() {
        return JudgePool.getInfo();
    }

    public PoolInfoVO updatePool(PoolInfoDTO poolInfoDTO) {
        return JudgePool.update(poolInfoDTO);
    }

    public PageVO<RecordVO> listRecord(ConditionDTO conditionDTO) {
        Query query = new Query();
        if (StringUtils.isPresent(conditionDTO.getProblemId())) {
            query.addCriteria(Criteria.where(MongoConst.PROBLEM_ID).is(conditionDTO.getProblemId()));
        }
        if (StringUtils.isPresent(conditionDTO.getContestId())) {
            query.addCriteria(Criteria.where(MongoConst.CONTEST_ID).is(conditionDTO.getContestId()));
        }

        long count = mongoTemplate.count(query, Record.class);

        query.with(QueryUtils.defaultSort());
        QueryUtils.skip(query, conditionDTO);
        List<Record> records = mongoTemplate.find(query, Record.class);

        return new PageVO<>(
                records.stream().map(RecordVO::of).toList(),
                count
        );
    }
}
