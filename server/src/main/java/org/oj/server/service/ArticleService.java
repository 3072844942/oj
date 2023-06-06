package org.oj.server.service;

import org.oj.server.dao.ArticleRepository;
import org.oj.server.dto.ArticleDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.entity.Article;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.EntityPermissionEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.ArticleHomeVO;
import org.oj.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:07
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public ArticleHomeVO insertOne(ArticleDTO articleDTO) {
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
        // 禁止添加公开的文章, 需要审核
        if (article.getStatus().equals(EntityStateEnum.PUBLIC))
            article.setStatus(EntityStateEnum.REVIEW);

        article = articleRepository.insert(article);

        return ArticleHomeVO.of(article);
    }

    public ArticleHomeVO updateOne(ArticleDTO articleDTO) {
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

        Article article = articleRepository.save(Article.of(articleDTO));

        return ArticleHomeVO.of(article);
    }

    public ArticleHomeVO deleteOne(ArticleDTO articleDTO) {
        // id为空
        if (StringUtils.isEmpty(articleDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        Optional<Article> byId = articleRepository.findById(articleDTO.getId());
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }
        articleRepository.deleteById(articleDTO.getId());
        return ArticleHomeVO.of(byId.get());
    }

    public PageVO<ArticleHomeVO> find(ConditionDTO conditionDTO) {
        // 查找所有人可见， 公开的文章
        Article article = Article.builder()
                .status(EntityStateEnum.PUBLIC)
                .permission(EntityPermissionEnum.PUBLIC)
                .build();
        // 优先指定， 更新时间降序
        Page<Article> all = articleRepository.findAll(Example.of(article), PageRequest.of(conditionDTO.getCurrent() - 1, conditionDTO.getSize(), Sort.by(
                Sort.Order.desc("top"),
                Sort.Order.desc("updateTime")
        )));
        long count = articleRepository.count(Example.of(article));
        return new PageVO<>(all.stream().map(ArticleHomeVO::of).toList(), count);
    }
}
