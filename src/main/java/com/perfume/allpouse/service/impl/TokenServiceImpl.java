package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.exception.ExceptionEnum;
import com.perfume.allpouse.model.dto.SignDto;
import com.perfume.allpouse.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl implements TokenService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignServiceImpl.class);

    private final TokenProvider tokenProvider;

    public TokenServiceImpl(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Override
    public SignDto createToken(HttpServletRequest request) {
        LOGGER.info("[createToken] 리프레쉬 토큰 사용해서 토큰 재발급");
        String token = tokenProvider.resolveToken(request);
        if(tokenProvider.validateToken(token)) {
            long id = tokenProvider.getId(tokenProvider.resolveToken(request));
            String role = tokenProvider.getRole(tokenProvider.resolveToken(request));
            SignDto signDto = tokenProvider.createToken(role, id);
            LOGGER.info("[createToken] 리프레쉬 토큰 사용해서 토큰 재발급 성공 {} ", signDto);
            return signDto;
        }
        else {
            LOGGER.info("[createToken] 리프레쉬 토큰 기간 만료 ");
            throw new CustomException(ExceptionEnum.EXPIRED_TOKEN);
        }
    }
}
