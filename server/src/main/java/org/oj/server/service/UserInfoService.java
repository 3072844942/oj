package org.oj.server.service;

import org.oj.server.dao.UserInfoRepository;
import org.oj.server.entity.UserInfo;
import org.oj.server.enums.StatusCodeEnum;
import org.oj.server.exception.ErrorException;
import org.oj.server.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author march
 * @since 2023/5/31 下午3:14
 */
@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;


    public Map<String, UserInfo> findAllById(List<String> ids) {
        List<UserInfo> infos = userInfoRepository.findAllById(ids);

        return infos.stream().collect(Collectors.toMap(UserInfo::getId, a -> a, (k1, k2) -> k1));
    }

    public UserInfoVO findById(String userId) {
        Optional<UserInfo> byId = userInfoRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new ErrorException(StatusCodeEnum.DATA_NOT_EXIST);
        }

        return UserInfoVO.of(byId.get());
    }
}
