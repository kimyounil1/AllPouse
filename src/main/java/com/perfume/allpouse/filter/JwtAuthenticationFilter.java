package com.perfume.allpouse.filter;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.exception.ExceptionEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Transactional
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request);
        LOGGER.info("[doFilterInternal] 토큰 정보 읽어오기 token : {} ",token);
        LOGGER.info("[doFilterInternal] 토큰 유효성 체크 ");
        try {
            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication); // 토큰이 유효하다면 sercuritycontextholder에 추가한다.
                LOGGER.info("[doFilterInternal] 토큰 유효성 체크 완료 ");
            }
        }
        catch (ExpiredJwtException e) {
            LOGGER.info("[doFilterInternal] 토큰 유효 기간 끝 ");
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(),token);

        }
        catch (JwtException e){
            LOGGER.info("[doFilterInternal] 토큰 시그니처 에러");
            throw new JwtException("토큰 시그니처 에러");
        }

        filterChain.doFilter(request, response);
    }
}
