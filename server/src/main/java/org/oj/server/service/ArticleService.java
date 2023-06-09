package org.oj.server.service;

import org.bson.types.ObjectId;
import org.oj.server.constant.HtmlConst;
import org.oj.server.constant.MongoConst;
import org.oj.server.dao.ArticleRepository;
import org.oj.server.dao.CategoryRepository;
import org.oj.server.dao.TagRepository;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Article;
import org.oj.server.entity.Tag;
import org.oj.server.entity.User;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.MongoTemplateUtils;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.QueryUtils;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.*;
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
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final MongoTemplateUtils mongoTemplateUtils;

    public ArticleService(ArticleRepository articleRepository, UserService userService, MongoTemplate mongoTemplate, TagRepository tagRepository, CategoryRepository categoryRepository, MongoTemplateUtils mongoTemplateUtils) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.mongoTemplateUtils = mongoTemplateUtils;
    }

    public ArticleDTO insertOne(ArticleDTO articleDTO) {
        ArticleDTO.check(articleDTO);
        // 置空
        articleDTO.setId(null);

        Article article = Article.of(articleDTO);
        // 设置作者
        article.setUserId(Request.user.get().getId());
        article.setState(EntityStateEnum.DRAFT);
        // 没有写权限
        PermissionUtil.checkTop(article, article.getIsTop());

        article = articleRepository.insert(article);
        return ArticleDTO.of(article);
    }

    public ArticleDTO updateOne(ArticleDTO articleDTO) {
        ArticleDTO.check(articleDTO);

        // id为空
        if (StringUtils.isEmpty(articleDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
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

        Article article = byId.get();

        article.setUserId(Request.user.get().getId());
        if (articleDTO.getTagIds() != null && articleDTO.getTagIds().size() != 0)
            article.setTagIds(articleDTO.getTagIds());
        if (StringUtils.isPresent(articleDTO.getCategoryId())) article.setCategoryId(articleDTO.getCategoryId());
        if (StringUtils.isPresent(articleDTO.getCover())) article.setCover(articleDTO.getCover());
        if (StringUtils.isPresent(articleDTO.getTitle())) article.setTitle(articleDTO.getTitle());
        if (StringUtils.isPresent(articleDTO.getSummary())) article.setSummary(articleDTO.getSummary());
        if (StringUtils.isPresent(articleDTO.getContent())) article.setContent(articleDTO.getContent());

        PermissionUtil.checkState(article, byId.get().getUserId(), articleDTO.getState());
        PermissionUtil.checkTop(article, articleDTO.getIsTop());

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

        mongoTemplateUtils.delete(id, Article.class);
    }

    public void delete(List<String> ids) {
        mongoTemplateUtils.delete(ids, Article.class);
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
                        mongoTemplate.findOne(new Query(Criteria.where(MongoConst.ID).lt(targetId))
                                .limit(1)
                                .with(Sort.by(Sort.Direction.DESC, MongoConst.ID)), Article.class)
                )
        );
        articleVO.setNext(
                ArticlePaginationVO.of(
                        mongoTemplate.findOne(new Query(Criteria.where(MongoConst.ID).gt(targetId))
                                .limit(1)
                                .with(Sort.by(Sort.Direction.ASC, MongoConst.ID)), Article.class)
                )
        );
        // 查询最新文章
        Page<Article> newList = articleRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Order.desc(MongoConst.UPDATE_TIME))));
        articleVO.setNewesList(newList.map(ArticleRecommendVO::of).toList());

        // 设置标签
        articleVO.setCategory(CategoryVO.of(categoryRepository.findById(article.getCategoryId()).get()));
        List<Tag> allById = tagRepository.findAllById(article.getTagIds());
        articleVO.setTags(
                allById.stream().map(TagVO::of).toList()
        );
        articleVO.setAuthor(UserProfileVO.of(userService.findById(article.getUserId())));

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

    public PageVO<ArticleSearchVO> find(ConditionDTO conditionDTO) {
//        elasticsearchOperations.search(new BaseQuery(), Article.class);
        // 构建一个原生搜索查询
//        NativeQuery build = new NativeQueryBuilder()
//                .withQuery(Queries.termQueryAsQuery("ad", "asd"))
//                .build();
//        elasticsearchOperations.search(, Article.class);
        return findInMongoDB(conditionDTO);
    }

    private PageVO<ArticleSearchVO> findInMongoDB(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = QueryUtils.defaultQuery(conditionDTO);

        // 匹配关键字
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where(MongoConst.TITLE).regex(keywords),
                    Criteria.where(MongoConst.CONTENT).regex(keywords),
                    Criteria.where(MongoConst.CATEGORY_ID).is(keywords)
            ));
        }
        if (conditionDTO.getTags() != null && conditionDTO.getTags().size() != 0) {
            query.addCriteria(Criteria.where(MongoConst.TAG_ID).in(conditionDTO.getTags()));
        }

        long count = mongoTemplate.count(query, Article.class);

        query.with(QueryUtils.defaultSort());
        QueryUtils.skip(query, conditionDTO);
        List<Article> all = mongoTemplate.find(query, Article.class);

        return new PageVO<>(
                // 设置高亮
                parse(all.stream().peek(article -> {
                    if (keywords != null) {
                        article.setTitle(article.getTitle().replaceAll(keywords, HtmlConst.PRE_TAG + keywords + HtmlConst.POST_TAG));
                        // 多次一举， 因为article要修改的是summary
                        int index = article.getContent().indexOf(keywords);
                        if (index != -1) {
                            article.setSummary(StringUtils.subKeywords(article.getContent(), keywords));
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
    private List<ArticleSearchVO> parse(List<Article> all) {
        List<String> userIds = all.stream().map(Article::getUserId).toList();
        Map<String, UserProfileVO> infoMap = userService.findAllById(userIds);

        return all.stream()
                .map(a -> {
                    ArticleSearchVO articleSearchDTO = ArticleSearchVO.of(a);

                    articleSearchDTO.setAuthor(infoMap.get(a.getUserId()));
                    articleSearchDTO.setCategory(CategoryVO.of(categoryRepository.findById(a.getCategoryId()).get()));
                    List<Tag> allById = tagRepository.findAllById(a.getTagIds());
                    articleSearchDTO.setTags(
                            allById.stream().map(TagVO::of).toList()
                    );
                    return articleSearchDTO;
                })
                .toList();
    }
}
