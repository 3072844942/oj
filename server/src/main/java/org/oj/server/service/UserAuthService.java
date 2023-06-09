package org.oj.server.service;

import org.oj.server.constant.RedisPrefixConst;
import org.oj.server.dao.UserAuthRepository;
import org.oj.server.dao.UserInfoRepository;
import org.oj.server.dto.UsernamePassword;
import org.oj.server.entity.UserAuth;
import org.oj.server.entity.UserInfo;
import org.oj.server.enums.EntityStateEnum;
import org.oj.server.enums.LoginTypeEnum;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.exception.WarnException;
import org.oj.server.util.EncryptionUtil;
import org.oj.server.util.JwtUtil;
import org.oj.server.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author march
 * @since 2023/5/31 下午3:14
 */
@Service
public class UserAuthService {
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RabbitMqService mqService;

    public String login(UsernamePassword usernamePassword) {
        Optional<UserAuth> byUsername = userAuthRepository.findByUsername(usernamePassword.getUsername());
        // 无用户
        if (byUsername.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        UserAuth userAuth = byUsername.get();
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
        return JwtUtil.create(userAuth.getPassword());
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
        if (userAuthRepository.existsByUsername(username).equals(true)) {
            throw new ErrorException(StatusCodeEnum.DATA_EXIST);
        }

        UserAuth userAuth = UserAuth.builder()
                .username(username)
                .password(EncryptionUtil.encode(usernamePassword.getPassword()))
                .loginType(LoginTypeEnum.EMAIL)
                .state(EntityStateEnum.PUBLIC)
                .lastLoginTime(System.currentTimeMillis())
                .build();
        String id = userAuthRepository.insert(userAuth).getId();
        UserInfo userInfo = UserInfo.builder().id(id)
                .email(username)
                .nickname(id)
                .build();
        userInfoRepository.insert(userInfo);

        // 返回token
        return JwtUtil.create(userAuth.getPassword());
    }
}
