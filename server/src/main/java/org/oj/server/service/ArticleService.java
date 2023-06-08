package org.oj.server.service;

import org.bson.types.ObjectId;
import org.oj.server.dao.ArticleRepository;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Article;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public PageVO<ArticleHomeVO> find(ConditionDTO conditionDTO) {
        // 查找公开的文章
        PageVO<Article> pageVO = find(EntityStateEnum.PUBLIC, conditionDTO);
        // 也许可以再多一些信息
        List<ArticleHomeVO> list = pageVO.getList().stream().map(ArticleHomeVO::of).toList();
        return new PageVO<>(list, pageVO.getTotal());
    }

    public void delete(ConditionDTO conditionDTO) {
        // 批量删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        articleRepository.deleteAllById(conditionDTO.getIds());
    }

    public ArticleDTO verifyOne(String id) {
        Article article = findById(id);

        // 不是待审核的文章
        if (!article.getState().equals(EntityStateEnum.REVIEW)) {
            throw new WarnException(StatusCodeEnum.FAIL);
        }

        // 设置公开
        article.setState(EntityStateEnum.PUBLIC);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO hideOne(String id) {
        Article article = findById(id);

        // 需要写权限
        if (!PermissionUtil.enableWrite(article.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        article.setState(EntityStateEnum.DRAFT);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO publishOne(String id) {
        Article article = findById(id);

        // 需要写权限
        if (!PermissionUtil.enableWrite(article.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        article.setState(EntityStateEnum.REVIEW);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO recycleOne(String id) {
        Article article = findById(id);

        // 需要写权限
        if (!PermissionUtil.enableWrite(article.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        article.setState(EntityStateEnum.DELETE);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
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
        return articleVO;
    }

    public PageVO<ArticleBackVO> findDraft(ConditionDTO conditionDTO) {
        // 查找草稿的文章
        PageVO<Article> pageVO = find(EntityStateEnum.DRAFT, conditionDTO);
        return new PageVO<>(pageVO.getList().stream().map(ArticleBackVO::of).toList(), pageVO.getTotal());
    }

    public PageVO<ArticleBackVO> findRecycle(ConditionDTO conditionDTO) {
        // 查找删除的文章
        PageVO<Article> pageVO = find(EntityStateEnum.DELETE, conditionDTO);
        return new PageVO<>(pageVO.getList().stream().map(ArticleBackVO::of).toList(), pageVO.getTotal());
    }

    public PageVO<?> findReview(ConditionDTO conditionDTO) {
        // 查找待审核的文章
        PageVO<Article> pageVO = find(EntityStateEnum.REVIEW, conditionDTO);
        return new PageVO<>(pageVO.getList().stream().map(ArticleBackVO::of).toList(), pageVO.getTotal());
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

    private PageVO<Article> find(EntityStateEnum state, ConditionDTO conditionDTO) {
        // 查找文章
        Article article = Article.builder().build();
        article.setState(state);

        // 指定了作者
        if (StringUtils.isPresent(conditionDTO.getUserId())) {
            article.setUserId(conditionDTO.getUserId());
        } else {
            // 不指定作者， 但是也没有读/写权限
            if (!PermissionUtil.enableRead(EntityStateEnum.PUBLIC, article.getUserId())) {
                throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
            }
        }

        WarnException checked = ConditionDTO.check(conditionDTO);
        if (checked != null) {
            throw checked;
        }

        return find(article, conditionDTO.getCurrent(), conditionDTO.getSize());
    }

    private PageVO<Article> find(Article article, Integer current, Integer size) {
        Example<Article> example = Example.of(article);
        long count = articleRepository.count(example);

        // 优先置顶， 更新时间降序
        Page<Article> all = articleRepository.findAll(example, PageRequest.of(current - 1, size, Sort.by(
                Sort.Order.desc("top"),
                Sort.Order.desc("updateTime")
        )));
        return new PageVO<>(all.toList(), count);
    }

    public PageVO<ArticleSearchDTO> find(ArticleDTO articleDTO, ConditionDTO conditionDTO) {
        WarnException checked = ConditionDTO.check(conditionDTO);
        if (checked != null) {
            throw checked;
        }

        // 查询条件
        Query query = new Query();
        if (articleDTO != null) {
            if (articleDTO.getUserId() != null)
                query.addCriteria(Criteria.where("userId").regex(articleDTO.getUserId()));
            if (articleDTO.getTitle() != null) query.addCriteria(Criteria.where("title").regex(articleDTO.getTitle()));
            if (articleDTO.getContent() != null)
                query.addCriteria(Criteria.where("content").regex(articleDTO.getContent()));
            if (articleDTO.getCategoryId() != null)
                query.addCriteria(Criteria.where("categoryId").is(articleDTO.getContent()));
            if (articleDTO.getTagIds() != null && articleDTO.getTagIds().size() != 0)
                query.addCriteria(Criteria.where("tagIds").in(articleDTO.getTagIds()));
            if (articleDTO.getState() != null)
                query.addCriteria(Criteria.where("state").is(EntityStateEnum.valueOf(articleDTO.getState())));
        }

        long count = mongoTemplate.count(query, Article.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Article> articles = mongoTemplate.find(query, Article.class);
        return new PageVO<>(
                articles.stream().map(ArticleSearchDTO::of).toList(),
                count
        );
    }
}
