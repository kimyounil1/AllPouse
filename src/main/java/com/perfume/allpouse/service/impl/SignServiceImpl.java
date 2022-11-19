package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.repository.UserRepository;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;



    public SignServiceImpl(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean signUp(String socialId, String userName, String role, int age, String gender, String loginType) {
        LOGGER.info("[signUp] 회원가입 시작 ");

        User user;
        Permission permission = Permission.ROLE_USER;
        if(role.equalsIgnoreCase("perfumer")) {
            permission = Permission.ROLE_PERFUMER;
        }

        user = User.builder()
                .socialId(socialId)
                .userName(userName)
                .permission(permission)
                .age(age)
                .gender(gender)
                .loginType(loginType)
                .userStatus("active")
                .build();

        User userSave = userRepository.save(user);
        LOGGER.info("[signUp] userEntity 값 확인 후 결과 주입");
        if(!userSave.getUserName().isEmpty()) {
            LOGGER.info("[signUp] 회원가입 완료");
            return true;
        }
        else {
            LOGGER.info("[signUp] 회원가입 실패");
            return false;
        }
    }

    @Override
    public String signIn(String socialId) {
        LOGGER.info("[signIn] User 정보 조회");
        User user = userRepository.findBySocialId(socialId);
        LOGGER.info("[signIn] User 정보 조회 완료 SocialId : {}", user.getSocialId());
        String token = tokenProvider.createToken(user.getSocialId(),user.getPermission().getValue());
        LOGGER.info("[signIn] token 발급 완료 token : {}", token);
        return token;
    }
}
