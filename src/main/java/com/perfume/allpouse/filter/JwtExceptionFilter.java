package com.perfume.allpouse.filter;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.exception.ExceptionEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (ExpiredJwtException ex) {
            LOGGER.info("[doFilterInternal] 토큰 유효 기간 예외 처리 ");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{ \"msg\" : \"" + ExceptionEnum.EXPIRED_TOKEN.getMsg()
                    + "\", \"code\" : \"" +  ExceptionEnum.EXPIRED_TOKEN.getCode()
                    + "\", \"success\" : \"" + ExceptionEnum.EXPIRED_TOKEN.isSuccess()
                    + "\", \"errors\" : \""+ ExceptionEnum.EXPIRED_TOKEN.name()+"\"}");
        }
        catch (JwtException e){
            LOGGER.info("[doFilterInternal] 토큰 시그니처 예외 처리 ");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("{ \"msg\" : \"" + ExceptionEnum.INVALID_TOKEN.getMsg()
                    + "\", \"code\" : \"" +  ExceptionEnum.INVALID_TOKEN.getCode()
                    + "\", \"success\" : \"" + ExceptionEnum.INVALID_TOKEN.isSuccess()
                    + "\", \"errors\" : \""+ ExceptionEnum.INVALID_TOKEN.name()+"\"}");
        }
    }
}
