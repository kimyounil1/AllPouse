package com.perfume.allpouse.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.perfume.allpouse.exception.ExceptionEnum.AUTHORITY_FORBIDDEN;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOGGER.info("[handle] 페이지 액세스 권한 없음 ");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            throw new CustomException(AUTHORITY_FORBIDDEN);
            //objectMapper.writeValue(os, new CustomException(AUTHORITY_FORBIDDEN));
            //os.flush();
        }
    }
}
