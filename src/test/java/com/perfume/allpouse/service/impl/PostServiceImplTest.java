package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.model.dto.PostResponseDto;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    }

    @Test
    @DisplayName("게시글 저장테스트2")
    @Transactional
    @Rollback(false)
    public void savePostTest2() throws Exception{

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

    }

    @Test
    @DisplayName("인기 게시글")
    @Transactional
    public void getPopularPostTest() throws Exception {
        //when
        List<PostResponseDto> popularPost = postService.getPopularPost(5);

        //then
        assertThat(popularPost.size()).isEqualTo(1);
        System.out.println(popularPost.get(0));

    }
}