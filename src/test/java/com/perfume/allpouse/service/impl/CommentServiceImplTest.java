package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.repository.CommentRepository;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;


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
        SaveCommentDto dto = new SaveCommentDto(
                15L, "new_title", "new_content", 3L, 12L
        );

        //when
        Long newId = commentService.update(dto);
        Comment comment = commentRepository.findById(newId).get();

        //then
        assertThat(newId).isEqualTo(15L);
        assertThat(comment.getTitle()).isEqualTo("new_title");
        assertThat(comment.getContent()).isEqualTo("new_content");

    }



}