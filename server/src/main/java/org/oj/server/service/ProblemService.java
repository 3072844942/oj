package org.oj.server.service;

import org.oj.server.constant.HtmlConst;
import org.oj.server.constant.MongoConst;
import org.oj.server.dao.ProblemRepository;
import org.oj.server.dao.TagRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.ProblemDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Problem;
import org.oj.server.entity.ProblemExample;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.MongoTemplateUtils;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.QueryUtils;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:13
 */
@Service
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final TagRepository tagRepository;
    private final MongoTemplateUtils mongoTemplateUtils;


    public ProblemService(ProblemRepository problemRepository, UserService userService, MongoTemplate mongoTemplate, TagRepository tagRepository, MongoTemplateUtils mongoTemplateUtils) {
        this.problemRepository = problemRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
        this.tagRepository = tagRepository;
        this.mongoTemplateUtils = mongoTemplateUtils;
    }

    public ProblemDTO insertOne(ProblemDTO problemDTO) {
        ProblemDTO.check(problemDTO);

        // id不为空
        if (StringUtils.isPresent(problemDTO.getId())) {
            // 数据已存在
            if (problemRepository.existsById(problemDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            problemDTO.setId(null);
        }

        Problem problem = Problem.of(problemDTO);
        // 设置作者
        if (Request.user.get() != null) {
            problem.setUserId(Request.user.get().getId());
        }
        problem.setState(EntityStateEnum.DRAFT);
        // 没有写权限

        problem = problemRepository.insert(problem);

        return ProblemDTO.of(problem);
    }

    public ProblemDTO updateOne(ProblemDTO problemDTO) {
        ProblemDTO.check(problemDTO);

        // id为空
        if (StringUtils.isEmpty(problemDTO.getId())) {
            throw new WarnException(StatusCodeEnum.FAILED_PRECONDITION);
        }
        Optional<Problem> byId = problemRepository.findById(problemDTO.getId());
        // 数据不存在
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }
        // 不是自己的， 或者没有写权限
        if (!PermissionUtil.enableWrite(byId.get().getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        Problem problem = byId.get();
        problem.setUserId(Request.user.get().getId());
        if (problemDTO.getTagIds() != null && problemDTO.getTagIds().size() != 0)
            problem.setTagIds(problemDTO.getTagIds());
        if (StringUtils.isPresent(problemDTO.getTitle())) problem.setTitle(problemDTO.getTitle());
        if (StringUtils.isPresent(problemDTO.getContent())) problem.setContent(problemDTO.getContent());
        if (StringUtils.isPresent(problemDTO.getInputContent())) problem.setInputContent(problemDTO.getInputContent());
        if (StringUtils.isPresent(problemDTO.getOutputContent()))
            problem.setOutputContent(problemDTO.getOutputContent());
        if (problemDTO.getExamples() != null && problemDTO.getExamples().size() != 0)
            problem.setExamples(problemDTO.getExamples().stream().map(ProblemExample::of).toList());
        if (StringUtils.isPresent(problemDTO.getIntro())) problem.setIntro(problem.getIntro());
        if (problemDTO.getTimeRequire() != 0) problem.setTimeRequire(problem.getTimeRequire());
        if (problemDTO.getMemoryRequire() != 0) problem.setMemoryRequire(problem.getMemoryRequire());
        if (problemDTO.getIsSpecial()) {
            problem.setIsSpecial(true);
            problem.setSpecialAddress(problemDTO.getSpecialAddress());
        }
        if (PermissionUtil.enableWrite("")) {
            problem.setState(EntityStateEnum.valueOf(problemDTO.getState()));
        } else {
            // 没有权限不允许立刻公开
            if (!EntityStateEnum.PUBLIC.equals(EntityStateEnum.valueOf(problemDTO.getState())))
                problem.setState(EntityStateEnum.valueOf(problemDTO.getState()));
        }

        // 保存
        problem = problemRepository.save(problem);
        return ProblemDTO.of(problem);
    }

    public void deleteOne(String id) {
        Problem problem = findById(id);

        mongoTemplateUtils.delete(id, Problem.class, problem.getUserId());
    }

    public void delete(List<String> ids) {
        mongoTemplateUtils.delete(ids, Problem.class);
    }

    public ProblemVO findOne(String id) {
        Problem problem = findById(id);
        // 无读权限
        if (!PermissionUtil.enableRead(problem.getState(), problem.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        ProblemVO problemVO = ProblemVO.of(problem);

        // 设置标签
        problemVO.setTags(
                tagRepository.findAllById(problem.getTagIds()).stream().map(TagVO::of).toList()
        );
        problemVO.setAuthor(UserProfileVO.of(userService.findById(problem.getUserId())));

        return problemVO;
    }

    public Problem findById(String id) {
        // id为空
        if (StringUtils.isEmpty(id)) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        Optional<Problem> byId = problemRepository.findById(id);
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        return byId.get();
    }

    private ProblemDTO updateOneState(String id, EntityStateEnum state) {
        Problem problem = findById(id);

        // 需要写权限
        if (!PermissionUtil.enableWrite(problem.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        problem.setState(state);
        problem = problemRepository.save(problem);

        return ProblemDTO.of(problem);
    }

    public PageVO<ProblemSearchVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = QueryUtils.defaultQuery(conditionDTO);

        // 匹配关键字
        String keywords = conditionDTO.getKeywords();
        QueryUtils.regexKeywords(query, keywords, MongoConst.TITLE, MongoConst.CONTENT);

        if (conditionDTO.getTags() != null && conditionDTO.getTags().size() != 0) {
            query.addCriteria(Criteria.where(MongoConst.TAG_ID).in(conditionDTO.getTags()));
        }

        long count = mongoTemplate.count(query, Problem.class);

        query.with(Sort.by(Sort.Order.desc(MongoConst.CREATE_TIME)));
        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Problem> all = mongoTemplate.find(query, Problem.class);

        return new PageVO<>(
                // 设置高亮
                parse(all.stream().peek(problem -> {
                    if (keywords != null) {
                        problem.setTitle(problem.getTitle().replaceAll(keywords, HtmlConst.PRE_TAG + keywords + HtmlConst.POST_TAG));
                        // 多次一举， 因为problem要修改的是summary
                        int index = problem.getContent().indexOf(keywords);
                        if (index != -1) {
                            problem.setContent(StringUtils.subKeywords(problem.getContent(), keywords));
                        }
                    }
                }).toList()),
                count
        );
    }

    /**
     * 转换数据对象
     *
     * @param all
     * @return
     */
    private List<ProblemSearchVO> parse(List<Problem> all) {
        List<String> userIds = all.stream().map(Problem::getUserId).toList();
        Map<String, UserProfileVO> infoMap = userService.findAllById(userIds);

        return all.stream()
                .map(a -> {
                    ProblemSearchVO problemSearchDTO = ProblemSearchVO.of(a);

                    problemSearchDTO.setAuthor(infoMap.get(a.getUserId()));
                    problemSearchDTO.setTags(
                            tagRepository.findAllById(a.getTagIds()).stream().map(TagVO::of).toList()
                    );
                    return problemSearchDTO;
                })
                .toList();
    }
}
