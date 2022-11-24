package com.perfume.allpouse.controller;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.model.dto.UserInfoDto;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.impl.ResponseServiceImpl;
import com.perfume.allpouse.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    private final TokenProvider tokenProvider;
    private final ResponseServiceImpl responseServiceImpl;

    private final UserServiceImpl userServiceImpl;

    public UserController(TokenProvider tokenProvider, ResponseServiceImpl responseServiceImpl, UserServiceImpl userServiceImpl) {
        this.tokenProvider = tokenProvider;
        this.responseServiceImpl = responseServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping(value = "user-info")
    public SingleResponse<UserInfoDto> userInfo(HttpServletRequest request) {
        LOGGER.info("[userInfo] user 정보 불러오기 ");
        long id = tokenProvider.getId(tokenProvider.resolveToken(request));
        UserInfoDto user = userServiceImpl.loadUserById(id).toUserInfoDto();
        return responseServiceImpl.getSingleResponse(user);
    }
}
