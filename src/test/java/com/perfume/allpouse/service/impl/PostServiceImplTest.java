package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostServiceImplTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("게시글 저장 테스트")
    @Transactional
    @Rollback(false)
    public void savePostTest() throws Exception {
        //given

        SavePostDto dto = SavePostDto.builder()
                .title("이건 테스트")
                .content("컨텐츠")
                .type("free")
                .userId(3L)
                .build();

        // when
        Long savedId = postService.save(dto);

        //then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("게시글 저장테스트2")
    @Transactional
    @Rollback(false)
    public void savePostTest2() throws Exception{
        //given
        Long userId = 4L;

        SavePostDto dto = SavePostDto.builder()
                .id(10L)
                .title("타이틀 변경")
                .content("컨텐츠 변경")
                .type("perfumer")
                .userId(userId)
                .build();

        //when
        Long savedId = postService.save(dto);

        //then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("사진있는 게시글 저장 테스트")
    @Transactional
    @Rollback(false)
    public void savePostWithPhotoTest() throws Exception{
        //given


        //when

        //then

    }


    @Test
    @DisplayName("게시글 삭제 테스트")
    @Transactional
    public void deletePostTest() throws Exception{
        //given
        SavePostDto dto = SavePostDto.builder()
                .title("test title")
                .content("test content")
                .type("free")
                .userId(4L)
                .build();

        //when
        Long savedId = postService.save(dto);
        Optional<Post> findByIdPost = postRepository.findById(savedId);
        assertThat(findByIdPost).isPresent();
        postService.delete(savedId);

        //then
        Optional<Post> findPost = postRepository.findById(savedId);
        assertThat(findPost).isNotPresent();
    }
}