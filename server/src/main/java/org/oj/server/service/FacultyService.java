package org.oj.server.service;

import org.oj.server.constant.MongoConst;
import org.oj.server.dao.FacultyRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.FacultyDTO;
import org.oj.server.entity.Faculty;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.QueryUtils;
import org.oj.server.vo.FacultyVO;
import org.oj.server.vo.PageVO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author march
 * @since 2023/5/31 下午3:08
 */
@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final MongoTemplate mongoTemplate;

    public FacultyService(FacultyRepository facultyRepository, MongoTemplate mongoTemplate) {
        this.facultyRepository = facultyRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public FacultyVO insertOne(FacultyDTO facultyDTO) {
        FacultyDTO.check(facultyDTO);
        // 不存在则置空
        facultyDTO.setId("");

        Faculty faculty = Faculty.of(facultyDTO);
        faculty = facultyRepository.insert(faculty);

        return FacultyVO.of(faculty);
    }

    public void delete(List<String> ids) {
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        facultyRepository.deleteAllById(ids);
    }

    public void deleteOne(String id) {
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }
        facultyRepository.deleteById(id);
    }

    public PageVO<FacultyVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();
        QueryUtils.regexKeywords(query, conditionDTO.getKeywords(), MongoConst.TITLE, MongoConst.CONTENT);

        long count = mongoTemplate.count(query, Faculty.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Faculty> categories = mongoTemplate.find(query, Faculty.class);
        return new PageVO<>(
                categories.stream().map(FacultyVO::of).toList(),
                count
        );
    }

    public FacultyDTO updateOne(FacultyDTO facultyDTO) {
        FacultyDTO.check(facultyDTO);

        // 数据已存在
        if (!facultyRepository.existsById(facultyDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Faculty faculty = Faculty.of(facultyDTO);
        faculty = facultyRepository.save(faculty);

        return FacultyDTO.of(faculty);
    }
}
