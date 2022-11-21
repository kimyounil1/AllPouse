package com.perfume.allpouse.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.exception.CustomeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.perfume.allpouse.exception.ExceptionEnum.AUTHORITY_FORBIDDEN;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOGGER.info("[handle] 페이지 액세스 권한 없음 ");
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(new CustomeException(AUTHORITY_FORBIDDEN)));

    }
}
