package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.service.SignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.ALREADY_SAVED_SOCIAL_ID;
import static com.perfume.allpouse.exception.ExceptionEnum.SOCIAL_ID_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class SignServiceImpl implements SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;



    public SignServiceImpl(UserRepository userRepository, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public boolean signUp(String socialId, String userName, String role, int age, String gender, String loginType) {
        LOGGER.info("[signUp] 회원가입 시작 ");

        User user;
        Permission permission = Permission.ROLE_USER;
        if(role.equalsIgnoreCase("perfumer")) {
            permission = Permission.ROLE_PERFUMER;
        }
        LOGGER.info("[signIn] User 정보 조회");
        validateDuplicateUserReturnFail(socialId, loginType);

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
    public String signIn(String socialId, String loginType) {
        LOGGER.info("[signIn] User 정보 조회");
        Optional<User> user = validateDuplicateUser(socialId, loginType);
        LOGGER.info("[signIn] User 정보 조회 완료 SocialId : {}", user.get().getSocialId());
        String token = tokenProvider.createToken(user.get().getSocialId(),user.get().getPermission().getValue(), user.get().getId());
        LOGGER.info("[signIn] token 발급 완료 token : {}", token);
        return token;
    }


    private Optional<User> validateDuplicateUser(String socialId, String loginType) {
        Optional<User> optionalUser = userRepository.findBySocialIdAndLoginType(socialId, loginType);
        if(optionalUser.isEmpty()) {
            LOGGER.info("[validateDuplicateUser] SOCIAL_ID 조회 결과 없음 : {}", socialId);
            throw new CustomException(SOCIAL_ID_NOT_FOUND);
        }
        return optionalUser;
    }

    private void validateDuplicateUserReturnFail(String socialId, String loginType) {
        Optional<User> optionalUser = userRepository.findBySocialIdAndLoginType(socialId, loginType);
        if(optionalUser.isPresent()) {
            LOGGER.info("[validateDuplicateUser] SOCIAL_ID 중복 : {}", socialId);
            throw new CustomException(ALREADY_SAVED_SOCIAL_ID);
        }
    }
}
