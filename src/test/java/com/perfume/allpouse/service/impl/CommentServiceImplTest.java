package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.repository.CommentRepository;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.service.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;


    @Transactional
    @DisplayName("댓글 저장 테스트")
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
    @DisplayName("댓글 업데이트 테스트")
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

    @Transactional
    @DisplayName("댓글 삭제 테스트")
    @Test
    public void deleteTest() throws Exception {
        //given
        SaveCommentDto dto = new SaveCommentDto(
                "testTitle", "testContent", 4L, 12L
        );

        Long savedId = commentService.save(dto);


        //when
        Optional<Comment> optionalComment
                = commentRepository.findById(savedId);
        Comment comment = optionalComment.get();

        //then
        assertThat(comment.getContent()).isEqualTo("testContent");

        commentService.delete(savedId);
        Assertions.assertThrows(IllegalStateException.class,
                ()->commentService.delete(savedId));
    }

    @Transactional
    @DisplayName("유저 Id로 댓글 조회 테스트")
    @Test
    public void findCommentsByUserIdTest() throws Exception{
        //given
        saveComment("title1", "content1", 3L, 13L);
        saveComment("title2", "content2", 3L, 12L);
        saveComment("title3", "content3", 4L, 13L);
        saveComment("title4", "content4", 5L, 12L);


        //when
        List<Comment> comments = commentService.findByUserId(5L);

        //then
        assertThat(comments.size()).isEqualTo(1);
    }

    @Transactional
    @DisplayName("리뷰 Id로 댓글 조회 테스트")
    @Test
    public void findCommentsByReviewIdTest() throws Exception {
        //given
        saveComment("title1", "content1", 3L, 13L);
        saveComment("title2", "content2", 3L, 12L);
        saveComment("title3", "content3", 4L, 13L);
        saveComment("title4", "content4", 5L, 12L);


        //when
        List<Comment> comments = commentService.findByReviewId(12L);

        //then
        assertThat(comments.size()).isEqualTo(3);
    }


    private void saveComment(String title1, String content1, long userId, long reviewId) {
        SaveCommentDto dto = new SaveCommentDto(title1, content1, userId, reviewId);
        commentService.save(dto);
    }


}