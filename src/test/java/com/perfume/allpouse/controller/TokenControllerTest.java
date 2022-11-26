package com.perfume.allpouse.controller;

import com.perfume.allpouse.model.dto.SignDto;
import com.perfume.allpouse.service.impl.TokenServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.reactive.server.StatusAssertions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(TokenController.class)
public class TokenControllerTest {


    MockMvc mvc;

    @Test
    @DisplayName("헬로 디티오 컨트롤러 테스트")
    void HelloControllerDtoTest() throws Exception {
        //given

        //when
/*        SignDto signDto = tokenServiceTest.saveTest();
        //then
        mvc.perform(get("/api/v1/refreshToken")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(signDto.getAccess_token()))
                .andExpect(jsonPath("$.refreshToken").value(signDto.getRefresh_token()));*/
    }


}
