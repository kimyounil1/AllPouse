package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.PostCommentRepository;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.model.dto.PostCommentResponseDto;
import com.perfume.allpouse.model.dto.SavePostCommentDto;
import com.perfume.allpouse.service.PostCommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class PostCommentServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCommentService postCommentService;

    @Autowired
    PostCommentRepository postCommentRepository;


    @Test
    @DisplayName("게시글 댓글 생성 테스트")
    @Transactional
    @Rollback(false)
    public void savePostCommentTest() throws Exception{
        //given
        Long postId = 60L;
        Long userId = 5L;
        Long referCommentId = 2283L;

        User user = userRepository.findById(userId).get();

        SavePostCommentDto dto = SavePostCommentDto.builder()
                .content("레퍼 댓글 테스트 컨텐츠")
                .userId(user.getId())
                .userName(user.getUserName())
                .postId(postId)
                .referCommentId(referCommentId)
                .build();

        Long savedId = postCommentService.save(dto);
        PostComment comment = postCommentService.findOne(savedId);

        Assertions.assertThat(comment.getReferCommentId()).isEqualTo(referCommentId);
        System.out.println(comment.getReferCommentId());
    }

    @Test
    @DisplayName("isRecommended 테스트")
    @Transactional
    public void recommendCheckTest() throws Exception{
        //given
        Long userId = 5L;
        Long postId = 60L;

        Long postCommentId = 2348L;

        PostComment comment = postCommentService.findOne(postCommentId);

        System.out.println(comment.getRecommendUserList());
    }


    @Test
    @DisplayName("게시글 댓글 조회 테스트")
    @Transactional
    public void findPostCommentTest_1() throws Exception{


    }

    @Test
    @DisplayName("게시글 댓글 추천 조회")
    @Transactional
    public void recommendCheckTest_2() throws Exception {
        //given

    }

}