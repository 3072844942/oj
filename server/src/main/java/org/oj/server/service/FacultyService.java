package org.oj.server.service;

import jakarta.annotation.PostConstruct;
import org.oj.server.dao.FacultyRepository;
import org.oj.server.dto.FacultyDTO;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.entity.Article;
import org.oj.server.entity.Faculty;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.PermissionUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.FacultyVO;
import org.oj.server.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author march
 * @since 2023/5/31 下午3:08
 */
@Service
public class FacultyService {
    public static final Map<String, Faculty> facultyMap = new HashMap<>();
    private final FacultyRepository facultyRepository;
    private final MongoTemplate mongoTemplate;

    public FacultyService(FacultyRepository facultyRepository, MongoTemplate mongoTemplate) {
        this.facultyRepository = facultyRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public FacultyVO insertOne(FacultyDTO facultyDTO) {
        WarnException checked = FacultyDTO.check(facultyDTO);
        if (checked != null) {
            throw checked;
        }

        // id不为空
        if (StringUtils.isPresent(facultyDTO.getId())) {
            // 数据已存在
            if (facultyRepository.existsById(facultyDTO.getId())) {
                throw new ErrorException(StatusCodeEnum.DATA_EXIST);
            }
            // 不存在则置空
            facultyDTO.setId("");
        }

        Faculty faculty = Faculty.of(facultyDTO);
        faculty = facultyRepository.insert(faculty);
        facultyMap.put(faculty.getId(), faculty);

        return FacultyVO.of(faculty);
    }

    public void delete(List<String> ids) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        facultyRepository.deleteAllById(ids);
        ids.forEach(facultyMap::remove);
    }

    public void deleteOne(String id) {
        // 删除需要写权限
        if (!PermissionUtil.enableWrite("")) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        facultyRepository.deleteById(id);
        facultyMap.remove(id);
    }

    public PageVO<FacultyVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("desc").regex(keywords),
                    Criteria.where("title").regex(keywords)
            ));
        }

        long count = mongoTemplate.count(query, Faculty.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Faculty> categories = mongoTemplate.find(query, Faculty.class);
        return new PageVO<>(
                categories.stream().map(FacultyVO::of).toList(),
                count
        );
    }

    public FacultyDTO updateOne(FacultyDTO facultyDTO) {
        WarnException checked = FacultyDTO.check(facultyDTO);
        if (checked != null) {
            throw checked;
        }

        // 数据已存在
        if (!facultyRepository.existsById(facultyDTO.getId())) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Faculty faculty = Faculty.of(facultyDTO);
        faculty = facultyRepository.insert(faculty);
        facultyMap.put(faculty.getId(), faculty);

        return FacultyDTO.of(faculty);
    }

    @PostConstruct
    private void init() {
        // 预读取所有分类， 减少查询时间
        List<Faculty> all = facultyRepository.findAll();
        all.forEach(faculty -> facultyMap.put(faculty.getId(), faculty));
    }
}
