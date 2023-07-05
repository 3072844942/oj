package org.oj.server.service;

import org.oj.server.constant.HtmlConst;
import org.oj.server.constant.MongoConst;
import org.oj.server.dao.PhotoAlbumRepository;
import org.oj.server.dao.PhotoRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.PhotoAlbumDTO;
import org.oj.server.entity.Article;
import org.oj.server.entity.Photo;
import org.oj.server.entity.PhotoAlbum;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.QueryUtils;
import org.oj.server.util.SensitiveUtils;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.PageVO;
import org.oj.server.vo.PhotoAlbumVO;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:13
 */
@Service
public class PhotoAlbumService {
    private final PhotoAlbumRepository albumRepository;

    private final PhotoRepository photoRepository;
    private final MongoTemplate mongoTemplate;

    public PhotoAlbumService(PhotoAlbumRepository albumRepository, PhotoRepository photoRepository, MongoTemplate mongoTemplate) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public PageVO<PhotoAlbumVO> list(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = QueryUtils.defaultQuery(conditionDTO);

        // 匹配关键字
        QueryUtils.regexKeywords(query, conditionDTO.getKeywords(), MongoConst.TITLE, MongoConst.CONTENT);

        long count = mongoTemplate.count(query, PhotoAlbum.class);

        query.with(QueryUtils.defaultSort());
        QueryUtils.skip(query, conditionDTO);
        List<PhotoAlbum> all = mongoTemplate.find(query, PhotoAlbum.class);

        return new PageVO<>(
                all.stream().map(PhotoAlbumVO::of).toList(),
                count
        );
    }

    public PhotoAlbumVO updateOne(PhotoAlbumDTO albumDTO) {
        PhotoAlbumDTO.check(albumDTO);

        // id为空
        if (StringUtils.isEmpty(albumDTO.getId())) {
            throw new WarnException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        Optional<PhotoAlbum> byId = albumRepository.findById(albumDTO.getId());
        // 数据不存在
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        PhotoAlbum photoAlbum = byId.get();
        if (StringUtils.isPresent(albumDTO.getTitle())) {
            photoAlbum.setTitle(SensitiveUtils.filter(albumDTO.getTitle()));
        }
        if (StringUtils.isPresent(albumDTO.getContent())) {
            photoAlbum.setContent(SensitiveUtils.filter(albumDTO.getContent()));
        }
        if (StringUtils.isPresent(albumDTO.getCover())) {
            photoAlbum.setCover(albumDTO.getCover());
        }
        PermissionUtil.checkState(photoAlbum, "", albumDTO.getState());

        photoAlbum = albumRepository.save(photoAlbum);
        return PhotoAlbumVO.of(photoAlbum);
    }

    public PhotoAlbumVO insertOne(PhotoAlbumDTO albumDTO) {
        PhotoAlbumDTO.check(albumDTO);

        // id不为空
        if (StringUtils.isPresent(albumDTO.getId())) {
            // 数据已存在
            if (albumRepository.existsById(albumDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            albumDTO.setId(null);
        }

        PhotoAlbum photoAlbum = PhotoAlbum.of(albumDTO);
        photoAlbum.setState(EntityStateEnum.DRAFT);

        photoAlbum = albumRepository.save(photoAlbum);
        return PhotoAlbumVO.of(photoAlbum);
    }

    public void deleteOne(String albumId) {
        Optional<PhotoAlbum> byId = albumRepository.findById(albumId);
        if (byId.isEmpty()) {
            throw new WarnException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        // 软删除
        PhotoAlbum photoAlbum = byId.get();
        photoAlbum.setState(EntityStateEnum.DELETE);
        albumRepository.save(photoAlbum);

        // 软删除其下的所有照片
        List<Photo> allByAlbumId = photoRepository.findAllByAlbumId(albumId);
        photoRepository.saveAll(allByAlbumId.stream().peek(i -> i.setState(EntityStateEnum.DELETE)).toList());
    }
}
