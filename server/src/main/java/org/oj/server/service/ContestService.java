package org.oj.server.service;

import org.oj.server.config.OJConfig;
import org.oj.server.constant.HtmlConst;
import org.oj.server.constant.MongoConst;
import org.oj.server.constant.RedisPrefixConst;
import org.oj.server.dao.ContestRepository;
import org.oj.server.dao.ProblemRepository;
import org.oj.server.dao.TagRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.ContestDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Record;
import org.oj.server.entity.*;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.FilePathEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.*;
import org.oj.server.vo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author march
 * @since 2023/5/31 下午3:08
 */
@Service
public class ContestService {
    private final ContestRepository contestRepository;
    private final UserService userService;
    private final ProblemRepository problemRepository;
    private final MongoTemplate mongoTemplate;
    private final OJConfig ojConfig;
    private final TagRepository tagRepository;
    private final MongoTemplateUtils mongoTemplateUtils;
    private final RedisService redisService;
    private final RabbitMqService mqService;

    public ContestService(ContestRepository contestRepository, UserService userService, ProblemRepository problemRepository, MongoTemplate mongoTemplate, OJConfig ojConfig, TagRepository tagRepository, MongoTemplateUtils mongoTemplateUtils, RedisService redisService, RabbitMqService mqService) {
        this.contestRepository = contestRepository;
        this.userService = userService;
        this.problemRepository = problemRepository;
        this.mongoTemplate = mongoTemplate;
        this.ojConfig = ojConfig;
        this.tagRepository = tagRepository;
        this.mongoTemplateUtils = mongoTemplateUtils;
        this.redisService = redisService;
        this.mqService = mqService;
    }

    public ContestInfoVO findOne(String contestId) {
        ContestInfoVO contestInfoVO = (ContestInfoVO) redisService.get(RedisPrefixConst.CONTEXT + contestId);
        // 预热
        if (contestInfoVO != null) {
            return contestInfoVO;
        } else {
            // 去数据库找
            Contest contest = findById(contestId);

            // 如果非公开
            if (!PermissionUtil.enableRead(contest.getState(), contest.getUserId())) {
                throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
            }

            contestInfoVO = ContestInfoVO.of(contest);
            contestInfoVO.setAuthor(UserProfileVO.of(userService.findById(contest.getUserId())));
            // (如果有权限 || 是自己的) ||  (是公开的， 且到时间)
            // System.currentTimeMillis() 是毫秒
            if (PermissionUtil.enableRead(EntityStateEnum.REVIEW, contest.getUserId()) || (System.currentTimeMillis() / 1000) >= contest.getStartTime()) {
                // 设置题目
                List<Problem> problems = problemRepository.findAllById(contest.getProblemIds());

                contestInfoVO.setProblems(problems.stream().map(problem -> {
                    ProblemProfileVO profileVO = ProblemProfileVO.of(problem);
                    profileVO.setTags(tagRepository.findAllById(problem.getTagIds()).stream()
                            .map(TagVO::of).toList());
                    return profileVO;
                }).toList());
            }

            return contestInfoVO;
        }
    }

    public PageVO<RankInfoVO> findOneRank(String contestId, ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 被预热
        if (redisService.hasKey(RedisPrefixConst.CONTEXT + contestId)) {
            long start = (conditionDTO.getCurrent() - 1L) * conditionDTO.getSize();
            // zset 是闭区间, 要-1
            long end = (long) conditionDTO.getCurrent() * conditionDTO.getSize() - 1;
            Map<Object, Double> objectDoubleMap = redisService.zReverseRangeWithScore(RedisPrefixConst.CONTEXT_RANK + contestId, start, end);

            return new PageVO<>(
                    objectDoubleMap.keySet().stream().map(i -> (RankInfoVO) i).toList(),
                    redisService.zSize(RedisPrefixConst.CONTEXT_RANK + contestId)
            );
        } else {
            Contest contest = findById(contestId);

            // 如果未开始
            if ((System.currentTimeMillis() / 1000) < contest.getStartTime()) {
                throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
            }

            // 根据过题数 || 罚时排序
            List<RankInfo> list = contest.getRank().values().stream().sorted((a, b) -> {
                // 优先根据过题数， 大的在前面
                if (!a.getCount().equals(b.getCount())) return a.getCount().compareTo(b.getCount());
                // 随后根据罚时 小的在前面
                if (!a.getPenalty().equals(b.getPenalty())) return -a.getPenalty().compareTo(b.getPenalty());
                // 避免 a == b 和 b == a
                return a.getUserId().compareTo(b.getUserId());
            }).toList();
            long count = list.size();

            int frontIndex = (conditionDTO.getCurrent() - 1) * conditionDTO.getSize();
            if (frontIndex >= count) {
                throw new ErrorException(StatusCodeEnum.INDEX_OUT_OF_BOUND);
            }

            List<RankInfo> rankInfos = list.subList(frontIndex, conditionDTO.getSize());
            Map<String, UserProfileVO> userMap = userService.findAllById(rankInfos.stream().map(RankInfo::getUserId).toList());


            return new PageVO<>(
                    rankInfos.stream().map(i -> {
                        RankInfoVO rankInfoVO = RankInfoVO.of(i);
                        // 设置用户
                        rankInfoVO.setUser(userMap.get(i.getUserId()));
                        // 根据比赛的题目顺序生成状态
                        rankInfoVO.setProblemStates(contest.getProblemIds().stream().map(j -> {
                            return ProblemStateVO.of(i.getProblemStateMap().get(j));
                        }).toList());
                        return rankInfoVO;
                    }).toList(),
                    count
            );
        }
    }


    public Contest findById(String contestId) {
        Optional<Contest> byId = contestRepository.findById(contestId);
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        return byId.get();
    }

    public PageVO<UserVO> findOneUser(String contestId, ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        Contest contest = findById(contestId);

        int frontIndex = (conditionDTO.getCurrent() - 1) * conditionDTO.getSize();
        if (frontIndex >= contest.getUserIds().size()) {
            throw new WarnException(StatusCodeEnum.INDEX_OUT_OF_BOUND);
        }

        List<String> ids = contest.getUserIds().subList(frontIndex, conditionDTO.getSize());

        return new PageVO<>(
                ids.stream().map(userService::findById).toList(),
                (long) contest.getUserIds().size()
        );
    }

    public PageVO<ContestProfileVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = QueryUtils.defaultQuery(conditionDTO);

        // 匹配关键字
        String keywords = conditionDTO.getKeywords();
        QueryUtils.regexKeywords(query, keywords, MongoConst.TITLE, MongoConst.CONTENT, MongoConst.CATEGORY_ID);

        if (conditionDTO.getTags() != null && conditionDTO.getTags().size() != 0) {
            query.addCriteria(Criteria.where(MongoConst.TAG_ID).in(conditionDTO.getTags()));
        }

        long count = mongoTemplate.count(query, Contest.class);

        query.with(QueryUtils.defaultSort());
        QueryUtils.skip(query, conditionDTO);
        List<Contest> all = mongoTemplate.find(query, Contest.class);

        return new PageVO<>(
                // 设置高亮
                parse(all.stream().peek(contest -> {
                    if (keywords != null) {
                        contest.setTitle(contest.getTitle().replaceAll(keywords, HtmlConst.PRE_TAG + keywords + HtmlConst.POST_TAG));
                        contest.setContent(StringUtils.subKeywords(contest.getContent(), keywords));
                    }
                }).toList()),
                count
        );
    }

    /**
     * 转换数据对象
     *
     * @param all 数据库比赛列表
     * @return 简略比赛信息列表
     */
    private List<ContestProfileVO> parse(List<Contest> all) {
        return all.stream()
                .map(a -> {
                    ContestProfileVO contestProfileVO = ContestProfileVO.of(a);
                    contestProfileVO.setAuthor(UserProfileVO.of(userService.findById(a.getUserId())));
                    if (Request.user.get() != null) {
                        contestProfileVO.setIsSignUp(a.getUserIds().contains(Request.user.get().getId()));
                    }
                    return contestProfileVO;
                })
                .toList();
    }

    public ContestDTO updateOne(ContestDTO contestDTO) {
        ContestDTO.check(contestDTO);

        // id为空
        if (StringUtils.isEmpty(contestDTO.getId())) {
            throw new WarnException(StatusCodeEnum.FAILED_PRECONDITION);
        }
        Optional<Contest> byId = contestRepository.findById(contestDTO.getId());
        // 数据不存在
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }
        // 不是自己的， 或者没有写权限
        if (!PermissionUtil.enableWrite(byId.get().getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        Contest contest = byId.get();
        contest.setUserId(Request.user.get().getId());
        if (StringUtils.isPresent(contestDTO.getTitle())) contest.setTitle(contestDTO.getTitle());
        if (StringUtils.isPresent(contestDTO.getContent())) contest.setContent(contestDTO.getContent());
        if (contestDTO.getStartTime() != 0) contest.setStartTime(contest.getStartTime());
        if (contestDTO.getEndTime() != 0) contest.setEndTime(contest.getEndTime());

        PermissionUtil.checkState(contest, "", contestDTO.getState());

        // 保存
        contest = contestRepository.save(contest);

        // 提前3分钟预热
        mqService.context(contest.getId(), contest.getStartTime() - 3 * 60);
        // 结束后3分钟保存
        mqService.context(contest.getId(), contest.getEndTime() + 3 * 60);
        return ContestDTO.of(contest);
    }

    public void deleteOne(String contestId) {
        Contest contest = findById(contestId);

        // 不是自己的， 或没有写权限
        mongoTemplateUtils.delete(contestId, Contest.class, contest.getUserId());
    }

    public void delete(List<String> ids) {
        mongoTemplateUtils.delete(ids, Contest.class);
    }

    public ContestDTO insertOne(ContestDTO contestDTO) {
        ContestDTO.check(contestDTO);

        contestDTO.setId(null);

        Contest contest = Contest.of(contestDTO);
        // 设置作者
        if (Request.user.get() != null) {
            contest.setUserId(Request.user.get().getId());
        }
        contest.setState(EntityStateEnum.DRAFT);

        contest = contestRepository.insert(contest);

        // 提前3分钟预热
        mqService.context(contest.getId(), contest.getStartTime() - 3 * 60);
        // 结束后3分钟保存
        mqService.context(contest.getId(), contest.getEndTime() + 3 * 60);
        return ContestDTO.of(contest);
    }

    public ContestProfileVO signUp(String contestId) {
        User userAuth = Request.user.get();
        if (userAuth == null) {
            throw new ErrorException(StatusCodeEnum.LOGIN_ERROR);
        }

        Contest contest = findById(contestId);
        if (contest.getUserIds().contains(userAuth.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_EXIST);
        }

        contest.getUserIds().add(userAuth.getId());
        contest = contestRepository.save(contest);

        ContestProfileVO contestProfileVO = ContestProfileVO.of(contest);
        contestProfileVO.setAuthor(UserProfileVO.of(userService.findById(contest.getUserId())));
        contestProfileVO.setIsSignUp(true);
        return contestProfileVO;
    }

    public String export(String contestId) {
        // 如果正在导出
        if (redisService.get(RedisPrefixConst.EXPORT_CONTEXT + contestId) != null) {
            throw new WarnException(StatusCodeEnum.RUNNING);
        }

        String path = ojConfig.getBase() + FilePathEnum.EXCEL.getPath() + contestId + ".xlsx";
        File file = new File(path);
        if (file.exists()) { // 已经生成过
            return ojConfig.getUrlBase() + FilePathEnum.EXCEL.getPath() + contestId + ".xlsx";
        }

        // 加锁
        redisService.set(RedisPrefixConst.EXPORT_CONTEXT + contestId, contestId);

        Contest contest = findById(contestId);
        if (contest.getEndTime() >= System.currentTimeMillis() / 1000) {
            throw new WarnException("比赛未结束");
        }

        Collection<RankInfo> values = contest.getRank().values();
        // 查找所有有记录的用户
        Map<String, UserProfileVO> userMap = userService.findAllById(values.stream().map(RankInfo::getUserId).toList());

        // 写入excel
        try (Excel excel = new Excel()) {
            // 排序，并组装
            List<RankInfoVO> voList = values.stream().sorted((a, b) -> {
                // 优先根据过题数， 大的在前面
                if (!a.getCount().equals(b.getCount())) return a.getCount().compareTo(b.getCount());
                // 随后根据罚时 小的在前面
                if (!a.getPenalty().equals(b.getPenalty())) return -a.getPenalty().compareTo(b.getPenalty());
                // 避免 a == b 和 b == a
                return a.getUserId().compareTo(b.getUserId());
            }).map(i -> {
                RankInfoVO rankInfoVO = RankInfoVO.of(i);
                // 设置用户
                rankInfoVO.setUser(userMap.get(i.getUserId()));
                // 根据比赛的题目顺序生成状态
                rankInfoVO.setProblemStates(contest.getProblemIds().stream().map(j -> {
                    return ProblemStateVO.of(i.getProblemStateMap().get(j));
                }).toList());
                return rankInfoVO;
            }).toList();

            // 查找题目名称
            List<String> titles = problemRepository.findAllById(contest.getProblemIds())
                    .stream().map(Problem::getTitle).toList();

            excel.addSheet("ranks",
                    RankInfoVO.getHeader(titles),
                    voList);

            excel.save(path);

            return ojConfig.getUrlBase() + FilePathEnum.EXCEL.getPath() + contestId + ".xlsx";
        } catch (IOException e) {
            throw new ErrorException(StatusCodeEnum.SYSTEM_ERROR);
        } finally {
            // 解锁
            redisService.del(RedisPrefixConst.EXPORT_CONTEXT + contestId);
        }
    }

    public ProblemVO findOneProblem(String contestId, String problemId) {
        // 被预热
        if (redisService.hasKey(RedisPrefixConst.CONTEXT + contestId)) {
            Object o = redisService.hGet(RedisPrefixConst.CONTEXT_PROBLEM + contestId, problemId);
            if (o == null) {
                throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
            }
            return (ProblemVO) o;
        } else {
            Optional<Problem> byId = problemRepository.findById(problemId);
            if (byId.isEmpty()) {
                throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
            }
            return ProblemVO.of(byId.get());
        }
    }

    /**
     * 保存判题记录
     *
     * @param record
     */
    public void save(Record record) {
        String contestId = record.getContestId();
        // 被预热
        Object o1 = redisService.get(RedisPrefixConst.CONTEXT + contestId);
        if (o1 != null) {
            ContestInfoVO contest = (ContestInfoVO) o1;

            // 找到旧的info
            Object o = redisService.hGet(RedisPrefixConst.CONTEXT_RANK_MAP + contestId, record.getUserId());

            RankInfoVO rankInfo;
            if (o == null) {
                rankInfo = RankInfoVO.builder()
                        .user(UserProfileVO.of(userService.findById(record.getUserId())))
                        .count(0)
                        .penalty(0L)
                        .problemStates(new ArrayList<>())
                        .build();
            } else {
                rankInfo = (RankInfoVO) o;
                // 删除排序中旧的数据
                redisService.zRem(RedisPrefixConst.CONTEXT_RANK + contestId, o);
            }

            int t = -1;
            for (int i = 0; i < rankInfo.getProblemStates().size(); i++) {
                if (rankInfo.getProblemStates().get(i).getProblemId().equals(record.getProblemId())) {
                    t = i;
                    break;
                }
            }
            ProblemStateVO problemStateVO;
            // 如果已经有过记录
            if (t != -1) {
                problemStateVO = rankInfo.getProblemStates().get(t);
                if (problemStateVO.getIsAccept()) { // 已经通过不记录
                    return;
                }
            } else {
                problemStateVO = new ProblemStateVO();
            }
            problemStateVO.setIsAccept(true);
            problemStateVO.setNumber(problemStateVO.getNumber() + 1);
            problemStateVO.setPenalty(problemStateVO.getPenalty() + System.currentTimeMillis() / 1000 - contest.getStartTime());

            rankInfo.setCount(rankInfo.getCount() + 1);
            rankInfo.setPenalty(rankInfo.getPenalty() + System.currentTimeMillis() / 1000 - contest.getStartTime());
            if (t != -1) {
                rankInfo.getProblemStates().remove(t);
            }
            // 这里一定会塞进去
            for (int i = 0; i < contest.getProblems().size(); i++) {
                if (contest.getProblems().get(i).getId().equals(record.getProblemId())) {
                    rankInfo.getProblemStates().add(i, problemStateVO);
                    break;
                }
            }

            redisService.hSet(RedisPrefixConst.CONTEXT_RANK_MAP, record.getUserId(), rankInfo);
            // 排序 过题数 * 大数 - 罚时
            // 过题数优先， 罚时越小越靠前
            redisService.zIncr(RedisPrefixConst.CONTEXT_RANK + contestId, rankInfo, rankInfo.getCount() * 1000000D - rankInfo.getPenalty());
        }
    }
}
