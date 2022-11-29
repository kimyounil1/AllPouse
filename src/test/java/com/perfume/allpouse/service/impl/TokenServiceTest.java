package com.perfume.allpouse.service.impl;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.model.dto.SignDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class TokenServiceTest {

    @Autowired
    TokenProvider tokenProvider;

    @Transactional
    @DisplayName("리프레쉬 토큰 저장 테스트")
    @Test
    public void saveTest() {
        //given
        long id = 3;
        String role = "USER";
        //when
        SignDto signDto = tokenProvider.createToken(role,id);

        //then
        assertThat(signDto.getAccessToken()).isNotNull();
        assertThat(signDto.getRefreshToken()).isNotNull();
        System.out.println(signDto);
    }



}
