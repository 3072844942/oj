package org.oj.server.service;

import org.oj.server.config.OJConfig;
import org.oj.server.constant.HtmlConst;
import org.oj.server.dao.ContestRepository;
import org.oj.server.dao.ProblemRepository;
import org.oj.server.dao.TagRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.ContestDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Contest;
import org.oj.server.entity.Problem;
import org.oj.server.entity.RankInfo;
import org.oj.server.entity.User;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.FilePathEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.Excel;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public ContestService(ContestRepository contestRepository, UserService userService, ProblemRepository problemRepository, MongoTemplate mongoTemplate, OJConfig ojConfig, TagRepository tagRepository) {
        this.contestRepository = contestRepository;
        this.userService = userService;
        this.problemRepository = problemRepository;
        this.mongoTemplate = mongoTemplate;
        this.ojConfig = ojConfig;
        this.tagRepository = tagRepository;
    }

    public ContestInfoVO findOne(String contestId) {
        Contest contest = findById(contestId);

        // 如果非公开
        if (!PermissionUtil.enableRead(contest.getState(), contest.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        ContestInfoVO contestInfoVO = ContestInfoVO.of(contest);
        contestInfoVO.setAuthor(UserProfileVO.of(userService.findById(contest.getUserId())));
        // (如果有权限 || 是自己的) ||  (是公开的， 且到时间)
        // System.currentTimeMillis() 是毫秒
        if (PermissionUtil.enableRead(EntityStateEnum.REVIEW, contest.getUserId()) || (System.currentTimeMillis() / 1000) >= contest.getStartTime()) {
            // 设置题目
            List<Problem> problems = problemRepository.findAllById(contest.getProblemIds());

            contestInfoVO.setProblems(problems.stream().map(problem -> {
                ProblemVO problemVO = ProblemVO.of(problem);
                problemVO.setTags(tagRepository.findAllById(problem.getTagIds()).stream()
                        .map(TagVO::of).toList());
                problemVO.setExamples(problem.getExamples().stream().map(ProblemExampleVO::of).toList());
                return problemVO;
            }).toList());
        }

        return contestInfoVO;
    }

    public PageVO<RankInfoVO> findOneRank(String contestId, ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

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
        Map<String, User> userMap = userService.findAllById(rankInfos.stream().map(RankInfo::getUserId).toList());


        return new PageVO<>(
                rankInfos.stream().map(i -> {
                    RankInfoVO rankInfoVO = RankInfoVO.of(i);
                    // 设置用户
                    rankInfoVO.setUser(UserProfileVO.of(userMap.get(i.getUserId())));
                    // 根据比赛的题目顺序生成状态
                    rankInfoVO.setProblemStates(contest.getProblemIds().stream().map(j -> {
                        return ProblemStateVO.of(i.getProblemStateMap().get(j));
                    }).toList());
                    return rankInfoVO;
                }).toList(),
                count
        );
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
            throw new ErrorException(StatusCodeEnum.INDEX_OUT_OF_BOUND);
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
        Query query = new Query();
        // 有读写权限
        if (PermissionUtil.enableRead(EntityStateEnum.DRAFT, "")) {
            // 随意读
            query.addCriteria(Criteria.where("state").is(EntityStateEnum.valueOf(conditionDTO.getState())));
        } else {
            EntityStateEnum state = EntityStateEnum.valueOf(conditionDTO.getState());
            // 如果读的不是公开
            if (!state.equals(EntityStateEnum.PUBLIC)) {
                throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
            } else {
                query.addCriteria(Criteria.where("state").is(state));
            }
        }

        // 指定了作者
        if (conditionDTO.getId() != null) {
            query.addCriteria(Criteria.where("userId").is(conditionDTO.getId()));
        }
        // 匹配关键字
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("title").regex(keywords),
                    Criteria.where("content").regex(keywords),
                    Criteria.where("categoryId").is(keywords)
            ));
        }
        if (conditionDTO.getTags() != null && conditionDTO.getTags().size() != 0) {
            query.addCriteria(Criteria.where("tagIds").in(conditionDTO.getTags()));
        }

        long count = mongoTemplate.count(query, Contest.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
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
        List<String> userIds = all.stream().map(Contest::getUserId).toList();

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
        if (PermissionUtil.enableWrite("")) {
            contest.setState(EntityStateEnum.valueOf(contestDTO.getState()));
        } else {
            // 没有权限不允许立刻公开
            if (!EntityStateEnum.PUBLIC.equals(EntityStateEnum.valueOf(contestDTO.getState())))
                contest.setState(EntityStateEnum.valueOf(contestDTO.getState()));
        }

        // 保存
        contest = contestRepository.save(contest);
        return ContestDTO.of(contest);
    }

    public void deleteOne(String contestId) {
        Contest contest = findById(contestId);

        // 不是自己的， 或没有写权限
        if (!PermissionUtil.enableWrite(contest.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        contestRepository.deleteById(contestId);
    }

    public void delete(List<String> ids) {
        // 批量删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        contestRepository.deleteAllById(ids);
    }

    public ContestDTO insertOne(ContestDTO contestDTO) {
        ContestDTO.check(contestDTO);

        // id不为空
        if (StringUtils.isPresent(contestDTO.getId())) {
            // 数据已存在
            if (contestRepository.existsById(contestDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            contestDTO.setId(null);
        }

        Contest contest = Contest.of(contestDTO);
        // 设置作者
        if (Request.user.get() != null) {
            contest.setUserId(Request.user.get().getId());
        }
        contest.setState(EntityStateEnum.DRAFT);

        contest = contestRepository.insert(contest);

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
        String path = ojConfig.getBase() + FilePathEnum.EXCEL.getPath() + contestId + ".xlsx";
        File file = new File(path);
        if (file.exists()) { // 已经生成过
            return ojConfig.getUrlBase() + FilePathEnum.EXCEL.getPath() + contestId + ".xlsx";
        }

        Contest contest = findById(contestId);
        if (contest.getEndTime() >= System.currentTimeMillis() / 1000) {
            throw new WarnException("比赛未结束");
        }

        Collection<RankInfo> values = contest.getRank().values();
        // 查找所有有记录的用户
        Map<String, User> userMap = userService.findAllById(values.stream().map(RankInfo::getUserId).toList());

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
                rankInfoVO.setUser(UserProfileVO.of(userMap.get(i.getUserId())));
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
        }
    }
}
