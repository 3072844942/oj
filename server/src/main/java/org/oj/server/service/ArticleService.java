package org.oj.server.service;

import org.oj.server.dao.ArticleRepository;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.Request;
import org.oj.server.entity.Article;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
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
            articleDTO.setId("");
        }

        Article article = Article.of(articleDTO);
        // 设置作者
        article.setUserId(Request.user.get().getId());
        article.setState(EntityStateEnum.DRAFT);

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
        // 数据不存在
        if (!articleRepository.existsById(articleDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Article article = Article.of(articleDTO);
        // 设置作者
        article.setUserId(Request.user.get().getId());
        article.setState(EntityStateEnum.DRAFT);

        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public void deleteOne(ConditionDTO conditionDTO) {
        String id = conditionDTO.getId();

        Article article = findById(id);

        // 不是自己的， 并且没有写权限
        if (!article.getUserId().equals(Request.user.get().getId()) && !Request.permission.get().equals(PermissionEnum.WRITE)) {
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
        if (!Request.permission.get().equals(PermissionEnum.WRITE)) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        articleRepository.deleteAllById(conditionDTO.getIds());
    }

    public ArticleDTO verifyOne(ConditionDTO conditionDTO) {
        Article article = findById(conditionDTO.getId());

        // 设置审核
        article.setState(EntityStateEnum.PUBLIC);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO hideOne(ConditionDTO conditionDTO) {
        Article article = findById(conditionDTO.getId());

        article.setState(EntityStateEnum.DRAFT);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO publishOne(ConditionDTO conditionDTO) {
        Article article = findById(conditionDTO.getId());

        article.setState(EntityStateEnum.REVIEW);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO recycleOne(ConditionDTO conditionDTO) {
        Article article = findById(conditionDTO.getId());

        article.setState(EntityStateEnum.DELETE);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleDTO recover(ConditionDTO conditionDTO) {
        Article article = findById(conditionDTO.getId());

        article.setState(EntityStateEnum.DRAFT);
        article = articleRepository.save(article);

        return ArticleDTO.of(article);
    }

    public ArticleVO findOne(ConditionDTO conditionDTO) {
        Article article = findById(conditionDTO.getId());
        // 无读权限
        if (!PermissionUtil.enableRead(article.getState(), article.getUserId())) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        ArticleVO articleVO = ArticleVO.of(article);
        // 查询上一条， 下一条
        articleVO.setLast(
                ArticlePaginationVO.of(
                        articleRepository.findFirstByUpdateTimeBeforeOrderByUpdateTimeDesc(articleVO.getUpdateTime())
                )
        );
        articleVO.setNext(
                ArticlePaginationVO.of(
                        articleRepository.findFirstByUpdateTimeAfterOrderByUpdateTime(articleVO.getUpdateTime())
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

        return find(article, conditionDTO.getCurrent(), conditionDTO.getSize());
    }

    private PageVO<Article> find(Article article, Integer current, Integer size) {
        Example<Article> example = Example.of(article);
        long count = articleRepository.count(example);

        if (current < 0 || current > current / size) {
            throw new ErrorException("页码超限");
        }
        if (size < 0 || size > 100) {
            throw new ErrorException("请求数量过大");
        }

        // 优先置顶， 更新时间降序
        Page<Article> all = articleRepository.findAll(example, PageRequest.of(current - 1, size, Sort.by(
                Sort.Order.desc("top"),
                Sort.Order.desc("updateTime")
        )));
        return new PageVO<>(all.toList(), count);
    }
}
