package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CommentRepositoryImplTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 가져오는 테스트 - DTO")
    @Transactional
    public void getCommentTest() throws Exception{
        //given

        //when

        //then

    }

}