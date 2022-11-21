package com.perfume.allpouse.controller;

import com.perfume.allpouse.model.dto.SignDto;
import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.impl.ResponseServiceImpl;
import com.perfume.allpouse.service.impl.SignServiceImpl;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sign-api")
public class SignController {

    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);

    private final SignServiceImpl signServiceImpl;

    private final ResponseServiceImpl responseServiceImpl;


    public SignController(SignServiceImpl signServiceImpl, ResponseServiceImpl responseServiceImpl) {
        this.signServiceImpl = signServiceImpl;
        this.responseServiceImpl = responseServiceImpl;
    }

    @PostMapping(value = "sign-in")
    public SingleResponse<SignDto> signIn(
            @ApiParam(value = "socialId", required = true) @RequestParam String socialId)
            throws RuntimeException {
        LOGGER.info("[signIn] Login 시도 중 ");
        SignDto signDto = SignDto.builder()
                .token(signServiceImpl.signIn(socialId))
                .build();
        return responseServiceImpl.getSingleResponse(signDto);
    }

    @PostMapping(value = "sign-up")
    public CommonResponse signUp(
            @ApiParam(value = "socialId", required = true) @RequestParam String socialId,
            @ApiParam(value = "이름", required = true) @RequestParam String userName,
            @ApiParam(value = "권한", required = true) @RequestParam String permission,
            @ApiParam(value = "나이", required = true) @RequestParam int age,
            @ApiParam(value = "성별", required = true) @RequestParam String gender) {


        LOGGER.info("[signUp] 회원가입 수행");
        CommonResponse response = new CommonResponse();
        responseServiceImpl.setSuccessResponse(response);
        if (!signServiceImpl.signUp(socialId,userName,permission,age,gender,"active")) {
            responseServiceImpl.setFalseResponse(response);
        }

        return response;
    }

}
