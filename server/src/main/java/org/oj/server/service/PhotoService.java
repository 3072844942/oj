package org.oj.server.service;

import org.oj.server.dao.PhotoRepository;
import org.oj.server.dto.PhotoDTO;
import org.oj.server.entity.Photo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.MongoTemplateUtils;
import org.oj.server.util.SensitiveUtils;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.PageVO;
import org.oj.server.vo.PhotoVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:13
 */
@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final MongoTemplateUtils mongoTemplateUtils;

    public PhotoService(PhotoRepository photoRepository, MongoTemplateUtils mongoTemplateUtils) {
        this.photoRepository = photoRepository;
        this.mongoTemplateUtils = mongoTemplateUtils;
    }

    public PageVO<PhotoVO> list(String albumId) {
        List<Photo> allByAlbumId = photoRepository.findAllByAlbumId(albumId);

        return new PageVO<>(
                allByAlbumId.stream().map(PhotoVO::of).toList(),
                (long) allByAlbumId.size()
        );
    }

    public void deleteOne(String photoId) {
        mongoTemplateUtils.delete(photoId, Photo.class);
    }

    public PhotoVO updateOne(PhotoDTO photoDTO) {
        Optional<Photo> byId = photoRepository.findById(photoDTO.getId());
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Photo photo = byId.get();
        if (StringUtils.isPresent(photoDTO.getTitle())) {
            photo.setTitle(SensitiveUtils.filter(photoDTO.getTitle()));
        }
        if (StringUtils.isPresent(photoDTO.getContent())) {
            photo.setContent(SensitiveUtils.filter(photoDTO.getContent()));
        }
        if (StringUtils.isPresent(photoDTO.getUrl())) {
            photo.setUrl(photoDTO.getTitle());
        }
        if (!photo.getState().getCode().equals(photoDTO.getState())) {
            photo.setState(EntityStateEnum.valueOf(photoDTO.getState()));
        }
        photoRepository.save(photo);
        return PhotoVO.of(photo);
    }

    public PhotoVO insertOne(PhotoDTO photoDTO) {
        PhotoDTO.check(photoDTO);

        if (photoRepository.existsById(photoDTO.getId())) {
            throw new WarnException(StatusCodeEnum.DATA_EXIST);
        }

        Photo photo = Photo.of(photoDTO);
        photo = photoRepository.insert(photo);
        return PhotoVO.of(photo);
    }
}
