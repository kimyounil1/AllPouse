package com.perfume.allpouse.controller;

import com.perfume.allpouse.model.dto.SignDto;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.impl.ResponseServiceImpl;
import com.perfume.allpouse.service.impl.TokenServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/token/")
public class TokenController {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);
    private final TokenServiceImpl tokenServiceImpl;

    private final ResponseServiceImpl responseServiceImpl;

    public TokenController(TokenServiceImpl tokenServiceImpl, ResponseServiceImpl responseServiceImpl) {
        this.tokenServiceImpl = tokenServiceImpl;
        this.responseServiceImpl = responseServiceImpl;
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, type = "string", paramType = "header", defaultValue = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzIiwicm9sZXMiOiJVU0VSIiwiaWF0IjoxNjY5NDQyOTkzLCJleHAiOjE2NzIwMzQ5OTN9.TJaNdLAJeWO7lS_zUg1siAz-EPvLDxK1fp7M3Ygg308")
    })
    @ApiOperation(value = "토큰 재발급" , notes = "Refresh Token을 이용한 토큰 재발급")
    @GetMapping(value = "refresh")
    public SingleResponse<SignDto> refresh(HttpServletRequest request) {
        LOGGER.info("[refreshToken] 토큰 재 발급 ");
        SignDto signDto = tokenServiceImpl.createToken(request);
        return responseServiceImpl.getSingleResponse(signDto);
    }
}
