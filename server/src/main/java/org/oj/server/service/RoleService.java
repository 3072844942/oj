package org.oj.server.service;

import org.oj.server.dao.PermissionRepository;
import org.oj.server.dao.RoleRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.RoleDTO;
import org.oj.server.entity.Permission;
import org.oj.server.entity.Role;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.PermissionEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.PageVO;
import org.oj.server.vo.RoleProfileVO;
import org.oj.server.vo.RoleVO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author march
 * @since 2023/5/31 下午3:14
 */
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final MongoTemplate mongoTemplate;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, MongoTemplate mongoTemplate, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.mongoTemplate = mongoTemplate;
        this.permissionRepository = permissionRepository;
    }

    public Map<String, RoleProfileVO> findAllById(Set<String> roleIds) {
        List<Role> allById = roleRepository.findAllById(roleIds);
        return allById.stream().map(RoleProfileVO::of).collect(Collectors.toMap(RoleProfileVO::getId, a -> a, (k1, k2) -> k1));
    }

    public void init() {
        // todo menuList
        Collection<Permission> values = permissionRepository.findAll();
        Map<String, PermissionEnum> all = new HashMap<>();
        values.forEach(i -> all.put(i.getId(), PermissionEnum.WRITE));

        Role root = new Role(
                "root",
                "根用户",
                EntityStateEnum.PUBLIC,
                null,
                all,
                0L, 0L
        );
        roleRepository.save(root);

        Role admin = new Role(
                "admin",
                "管理员",
                EntityStateEnum.PUBLIC,
                null,
                all,
                0L, 0L
        );
        roleRepository.save(admin);


        Map<String, PermissionEnum> map = new HashMap<>();
        values.forEach(i -> {
            String method = i.getMethod();
            if (method.equals("GET")) {
                map.put(i.getId(), PermissionEnum.WRITE);
            } else {
                map.put(i.getId(), PermissionEnum.NONE);
            }
        });
        Role user = new Role(
                "user",
                "普通用户",
                EntityStateEnum.PUBLIC,
                null,
                map,
                0L, 0L
        );
        roleRepository.save(user);
    }

    public void deleteOne(String roleId) {
        roleRepository.deleteById(roleId);
    }


    public RoleDTO insertOne(RoleDTO roleDTO) {
        RoleDTO.check(roleDTO);

        roleDTO.setId(null);

        Role role = Role.of(roleDTO);

        roleRepository.insert(role);
        return RoleDTO.of(role);
    }

    public RoleDTO updateOne(RoleDTO roleDTO) {
        RoleDTO.check(roleDTO);

        // 数据不存在
        Optional<Role> byId = roleRepository.findById(roleDTO.getId());
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        Role role = byId.get();
        if (StringUtils.isPresent(roleDTO.getName())) role.setName(roleDTO.getName());
        if (!roleDTO.getState().equals(role.getState().getCode()))
            role.setState(EntityStateEnum.valueOf(roleDTO.getState()));
        if (roleDTO.getMenuIds() != null && roleDTO.getMenuIds().size() != 0) role.setMenuIds(roleDTO.getMenuIds());
        if (roleDTO.getPermissionIds() != null && roleDTO.getPermissionIds().size() != 0) {
            Map<String, PermissionEnum> permissionEnumMap = new HashMap<>();
            for (int i = 0; i < roleDTO.getPermissionIds().size(); i++) {
                permissionEnumMap.put(
                        roleDTO.getPermissionIds().get(i),
                        PermissionEnum.valueOf(roleDTO.getPermissionStates().get(i))
                );
            }
            role.setPermissionIds(permissionEnumMap);
        }

        roleRepository.insert(role);
        return RoleDTO.of(role);
    }

    public PageVO<RoleProfileVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        Query query = new Query();
        String keywords = conditionDTO.getKeywords();
        if (StringUtils.isPresent(keywords)) {
            query.addCriteria(Criteria.where("name").regex(keywords));
        }

        long count = mongoTemplate.count(query, Role.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<Role> roles = mongoTemplate.find(query, Role.class);

        return new PageVO<>(
                roles.stream().map(RoleProfileVO::of).toList(),
                count
        );
    }

    public RoleVO findOne(String roleId) {
        Optional<Role> byId = roleRepository.findById(roleId);
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }
        return RoleVO.of(byId.get());
    }
}
