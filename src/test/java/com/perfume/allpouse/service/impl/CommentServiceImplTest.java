package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.service.CommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    CommentService commentService;

    @Transactional
    @DisplayName("저장 테스트")
    @Test
    public void saveTest() throws Exception {
        //given
        SaveCommentDto dto = new SaveCommentDto(
                "title", "content", 3L, 12L);


        //when
        Long savedId = commentService.save(dto);


        //then
        assertThat(savedId).isNotNull();
    }

    @Transactional
    @DisplayName("업데이트 테스트")
    @Test
    public void updateTest() throws Exception {
        //given


        //when

        //then
    }



}