package org.oj.server.service;

import org.oj.server.constant.RedisPrefixConst;
import org.oj.server.dao.FacultyRepository;
import org.oj.server.dao.UserRepository;
import org.oj.server.dto.ConditionDTO;
import org.oj.server.dto.UserInfoDTO;
import org.oj.server.dto.UsernamePassword;
import org.oj.server.entity.User;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.LoginTypeEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.EncryptionUtil;
import org.oj.server.util.JwtUtil;
import org.oj.server.util.StringUtils;
import org.oj.server.vo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author march
 * @since 2023/5/31 下午3:14
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final RedisService redisService;
    private final RabbitMqService mqService;
    private final RoleService roleService;
    private final FacultyRepository facultyRepository;

    public UserService(UserRepository userRepository, MongoTemplate mongoTemplate, RedisService redisService, RabbitMqService mqService, RoleService roleService, FacultyRepository facultyRepository) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.redisService = redisService;
        this.mqService = mqService;
        this.roleService = roleService;
        this.facultyRepository = facultyRepository;
    }

    public String login(UsernamePassword usernamePassword) {
        Optional<User> byUsername = userRepository.findByUsername(usernamePassword.getUsername());
        // 无用户
        if (byUsername.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        User userAuth = byUsername.get();
        // 用户被禁用
        if (userAuth.getState().equals(EntityStateEnum.REVIEW)) {
            throw new ErrorException(StatusCodeEnum.UNAUTHORIZED);
        }

        boolean matched = EncryptionUtil.match(usernamePassword.getPassword(), userAuth.getPassword());
        // 密码错误
        if (!matched) {
            throw new ErrorException(StatusCodeEnum.PASSWORD_NOT_MATCHED);
        }

        // 返回token
        return JwtUtil.create(userAuth.getId());
    }


    public void sendCode(UsernamePassword usernamePassword) {
        String username = usernamePassword.getUsername();
        boolean checked = StringUtils.checkEmail(username);
        // 非邮箱
        if (!checked) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        if (redisService.get(username) != null) {
            throw new WarnException(StatusCodeEnum.FAIL.getCode(), "请在" + redisService.getExpire(username) + "秒后重新发送");
        }

        // 五分钟过期
        String code = StringUtils.getRandomCode(6);
        redisService.set(RedisPrefixConst.TOKEN + username, code, 5 * 60);
        mqService.email(username, code, 5L);
    }

    @Transactional
    public String register(UsernamePassword usernamePassword) {
        String username = usernamePassword.getUsername();
        boolean checked = StringUtils.checkEmail(username);
        // 非邮箱
        if (!checked) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        String code = (String) redisService.get(RedisPrefixConst.TOKEN + username);
        // 验证码错误
        if (code == null || !code.equals(usernamePassword.getCode())) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        // 用户已存在
        if (userRepository.existsByUsername(username).equals(true)) {
            throw new ErrorException(StatusCodeEnum.DATA_EXIST);
        }

        User userAuth = User.builder()
                .username(username)
                .email(username)
                .password(EncryptionUtil.encode(usernamePassword.getPassword()))
                .loginType(LoginTypeEnum.EMAIL)
                .state(EntityStateEnum.PUBLIC)
                .build();
        String id = userRepository.insert(userAuth).getId();

        // 返回token
        return JwtUtil.create(id);
    }

    public String forget(UsernamePassword usernamePassword) {
        String username = usernamePassword.getUsername();
        boolean checked = StringUtils.checkEmail(username);
        // 非邮箱
        if (!checked) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        String code = (String) redisService.get(RedisPrefixConst.TOKEN + username);
        // 验证码错误
        if (code == null || !code.equals(usernamePassword.getCode())) {
            throw new ErrorException(StatusCodeEnum.FAILED_PRECONDITION);
        }

        // 用户不存在
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        // 修改数据
        User userAuth = byUsername.get();
        userAuth.setPassword(EncryptionUtil.encode(usernamePassword.getPassword()));
        userRepository.save(userAuth);

        // 返回token
        return JwtUtil.create(userAuth.getId());
    }


    public Map<String, User> findAllById(List<String> ids) {
        List<User> infos = userRepository.findAllById(ids);

        return infos.stream().collect(Collectors.toMap(User::getId, a -> a, (k1, k2) -> k1));
    }

    public UserVO findById(String userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        User userInfo = byId.get();
        UserVO of = UserVO.of(userInfo);
        of.setFaculty(FacultyVO.of(facultyRepository.findById(userInfo.getFacultyId()).get()));
        return of;
    }

    public UserInfoDTO updateOne(UserInfoDTO userInfoDTO) {
        Optional<User> byId = userRepository.findById(userInfoDTO.getId());
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        User userInfo = byId.get();
        if (StringUtils.isPresent(userInfoDTO.getNickname())) userInfo.setNickname(userInfoDTO.getNickname());
        if (StringUtils.isPresent(userInfoDTO.getAvatar())) userInfo.setAvatar(userInfoDTO.getAvatar());
        if (StringUtils.isPresent(userInfoDTO.getIntro())) userInfo.setIntro(userInfoDTO.getIntro());
        if (StringUtils.isPresent(userInfoDTO.getWebSite())) userInfo.setWebSite(userInfoDTO.getWebSite());
        if (userInfoDTO.getIsDisable() != userInfo.getIsDisable()) userInfo.setIsDisable(userInfoDTO.getIsDisable());
        if (userInfoDTO.getRoleIds() != null && userInfoDTO.getRoleIds().size() != 0)
            userInfo.setRoleIds(userInfoDTO.getRoleIds());
        // 真实信息需要审核
        if (StringUtils.isPresent(userInfoDTO.getNumber())) {
            userInfo.setNickname(userInfoDTO.getNickname());
            userInfo.setState(EntityStateEnum.REVIEW);
        }
        if (StringUtils.isPresent(userInfoDTO.getName())) {
            userInfo.setNickname(userInfoDTO.getNickname());
            userInfo.setState(EntityStateEnum.REVIEW);
        }
        if (StringUtils.isPresent(userInfoDTO.getFacultyId())) {
            userInfo.setNickname(userInfoDTO.getNickname());
            userInfo.setState(EntityStateEnum.REVIEW);
        }
        // 修改状态
        EntityStateEnum state = EntityStateEnum.valueOf(userInfoDTO.getState());
        if (state.equals(userInfo.getState())) {
            userInfo.setState(state);
        }

        userRepository.save(userInfo);
        return UserInfoDTO.of(userInfo);
    }


    public PageVO<UserVO> find(ConditionDTO conditionDTO) {
        ConditionDTO.check(conditionDTO);

        // 查询条件
        Query query = new Query();

        // 指定了作者
        if (conditionDTO.getFacultyId() != null) {
            query.addCriteria(Criteria.where("facultyId").is(conditionDTO.getFacultyId()));
        }
        // 匹配关键字
        String keywords = conditionDTO.getKeywords();
        if (keywords != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("ipAddress").regex(keywords),
                    Criteria.where("nickname").regex(keywords),
                    Criteria.where("name").regex(keywords),
                    Criteria.where("number").is(keywords)
            ));
        }
        if (conditionDTO.getIds() != null && conditionDTO.getIds().size() != 0) {
            query.addCriteria(Criteria.where("roleIds").in(conditionDTO.getId()));
        }

        long count = mongoTemplate.count(query, User.class);

        query.skip((conditionDTO.getCurrent() - 1L) * conditionDTO.getSize()).limit(conditionDTO.getSize());
        List<User> all = mongoTemplate.find(query, User.class);

        Set<String> roleIds = new HashSet<>();
        all.forEach(i -> roleIds.addAll(i.getRoleIds()));
        Map<String, RoleProfileVO> roleVOMap = roleService.findAllById(roleIds);

        return new PageVO<>(
                all.stream().map(i -> {
                    UserVO of = UserVO.of(i);
                    of.setRoles(i.getRoleIds().stream().map(roleVOMap::get).toList());
                    return of;
                }).toList(),
                count
        );
    }
}
