package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostServiceImplTest {

    @Autowired
    PostService postService;

    @Test
    @DisplayName("게시글 저장 테스트")
    @Transactional
    public void savePostTest() throws Exception {
        //given

        //when

        //then


    }

}