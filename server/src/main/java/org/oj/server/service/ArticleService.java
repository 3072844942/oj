package org.oj.server.service;

import org.bson.types.ObjectId;
import org.oj.server.constant.HtmlConst;
import org.oj.server.dao.ArticleRepository;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Article;
import org.oj.server.entity.UserInfo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * @since 2023/5/31 下午3:07
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public ArticleDTO insertOne(ArticleDTO articleDTO) {
        WarnException checked = ArticleDTO.check(articleDTO);
        if (checked != null) {
            throw checked;
        }
        // id不为空
        if (StringUtils.isPresent(articleDTO.getId())) {
            // 数据已存在
            if (articleRepository.existsById(articleDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            articleDTO.setId(null);
        }

        Article article = Article.of(articleDTO);
        // 设置作者
        if (Request.user.get() != null) {
            article.setUserId(Request.user.get().getId());
        }
        article.setState(EntityStateEnum.DRAFT);
        // 没有写权限
        if (!PermissionUtil.enableWrite("")) {
            article.setIsTop(false);
        }

        article = articleRepository.insert(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO updateOne(ArticleDTO articleDTO) {
        WarnException checked = ArticleDTO.check(articleDTO);
        if (checked != null) {
            throw checked;
        }
        // id为空
        if (StringUtils.isEmpty(articleDTO.getId())) {
            throw new WarnException(StatusCodeEnum.FAILED_PRECONDITION);
        }
        Optional<Article> byId = articleRepository.findById(articleDTO.getId());
        // 数据不存在
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }
        // 不是自己的， 或者没有写权限
        if (!PermissionUtil.enableWrite(byId.get().getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        Article article = Article.of(articleDTO);
        // 重置为草稿
        article.setState(EntityStateEnum.DRAFT);
        // 没有写权限
        if (!PermissionUtil.enableWrite("")) {
            article.setIsTop(false);
        }
        // 保存
        article = articleRepository.save(article);
        return ArticleDTO.of(article);
    }

    public void deleteOne(String id) {
        Article article = findById(id);

        // 不是自己的， 或没有写权限
        if (!PermissionUtil.enableWrite(article.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        articleRepository.deleteById(id);
    }

    public void delete(List<String> ids) {
        // 批量删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        articleRepository.deleteAllById(ids);
    }

    public ArticleDTO verifyOne(String id) {
        return updateOneState(id, EntityStateEnum.PUBLIC);
    }

    public ArticleDTO hideOne(String id) {
        return updateOneState(id, EntityStateEnum.DRAFT);
    }

    public ArticleDTO publishOne(String id) {
        return updateOneState(id, EntityStateEnum.REVIEW);
    }

    public ArticleDTO recycleOne(String id) {
        return updateOneState(id, EntityStateEnum.DELETE);
    }

    public ArticleVO findOne(String id) {
        Article article = findById(id);
        // 无读权限
        if (!PermissionUtil.enableRead(article.getState(), article.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        ArticleVO articleVO = ArticleVO.of(article);

        ObjectId targetId = new ObjectId(article.getId());
        // 查询上一条， 下一条
        articleVO.setLast(
                ArticlePaginationVO.of(
                        mongoTemplate.findOne(new Query(Criteria.where("_id").lt(targetId))
                                .limit(1)
                                .with(Sort.by(Sort.Direction.DESC, "_id")), Article.class)
                )
        );
        articleVO.setNext(
                ArticlePaginationVO.of(
                        mongoTemplate.findOne(new Query(Criteria.where("_id").gt(targetId))
                                .limit(1)
                                .with(Sort.by(Sort.Direction.ASC, "_id")), Article.class)
                )
        );
        // 查询最新文章
        Page<Article> newList = articleRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Order.desc("updateTime"))));
        articleVO.setNewesList(newList.map(ArticleRecommendVO::of).toList());

        // 设置标签
        articleVO.setCategory(CategoryVO.of(CategoryService.categoryMap.get(article.getCategoryId())));
        articleVO.setTags(
                article.getTagIds().stream().map(tagId -> TagVO.of(TagService.tagMap.get(tagId))).toList()
        );
        articleVO.setAuthor(UserProfileVO.of(userInfoService.findById(article.getUserId())));

        return articleVO;
    }

    private Article findById(String id) {
        // id为空
        if (StringUtils.isEmpty(id)) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        Optional<Article> byId = articleRepository.findById(id);
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        return byId.get();
    }

    private ArticleDTO updateOneState(String id, EntityStateEnum state) {
        Article article = findById(id);

        // 需要写权限
        if (!PermissionUtil.enableWrite(article.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        article.setState(state);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public PageVO<ArticleSearchDTO> find(ConditionDTO conditionDTO) {
        WarnException checked = ConditionDTO.check(conditionDTO);
        if (checked != null) {
            throw checked;
        }

        // 查询条件
        Query query = new Query();
        // 如果是自己的 || 有读写权限
        if (PermissionUtil.enableRead(EntityStateEnum.DRAFT, conditionDTO.getId())) {
            query.addCriteria(Criteria.where("state").is(EntityStateEnum.valueOf(conditionDTO.getState())));
        } else {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
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

        long count = mongoTemplate.count(query, Article.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Article> all = mongoTemplate.find(query, Article.class);

        return new PageVO<>(
                // 设置高亮
                parse(all.stream().peek(article -> {
                    if (keywords != null) {
                        article.setTitle(article.getTitle().replaceAll(keywords, HtmlConst.PRE_TAG + keywords + HtmlConst.POST_TAG));
                        int index = article.getContent().indexOf(keywords);
                        if (index != -1) {
                            // 获取关键词前面的文字
                            int preIndex = index > 25 ? index - 25 : 0;
                            String preText = article.getContent().substring(preIndex, index);
                            // 获取关键词到后面的文字
                            int last = index + keywords.length();
                            int postLength = article.getContent().length() - last;
                            int postIndex = postLength > 175 ? last + 175 : last + postLength;
                            String postText = article.getContent().substring(index, postIndex);
                            // 文章内容高亮
                            String articleContent = (preText + postText).replaceAll(keywords, HtmlConst.PRE_TAG + keywords + HtmlConst.POST_TAG);
                            article.setSummary(articleContent);
                        }
                    }
                }).toList()),
                count
        );
    }

    private List<ArticleSearchDTO> parse(List<Article> all) {
        List<String> userIds = all.stream().map(Article::getUserId).toList();
        Map<String, UserInfo> infoMap = userInfoService.findAllById(userIds);

        return all.stream()
                .map(a -> {
                    ArticleSearchDTO articleSearchDTO = ArticleSearchDTO.of(a);

                    articleSearchDTO.setAuthor(UserProfileVO.of(infoMap.get(a.getUserId())));
                    articleSearchDTO.setTags(
                            a.getTagIds().stream().map(tagId -> TagVO.of(TagService.tagMap.get(tagId))).toList()
                    );
                    articleSearchDTO.setCategory(CategoryVO.of(CategoryService.categoryMap.get(a.getCategoryId())));
                    return articleSearchDTO;
                })
                .toList();
    }
}
