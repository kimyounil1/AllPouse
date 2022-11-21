package com.perfume.allpouse.filter;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.exception.CustomeException;
import com.perfume.allpouse.model.reponse.EntryPointErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.perfume.allpouse.exception.ExceptionEnum.SOCIAL_ID_NOT_FOUND;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOGGER.info("[commence] 인증 실패 ");

        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(new CustomeException(SOCIAL_ID_NOT_FOUND)));
    }

}
