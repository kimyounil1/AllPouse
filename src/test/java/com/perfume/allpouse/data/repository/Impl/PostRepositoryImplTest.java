package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.model.dto.BannerResponseDto;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.perfume.allpouse.model.enums.Permission.*;

@SpringBootTest
class PostRepositoryImplTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Test
    @DisplayName("게시글 검색 테스트")
    public void basic_post_search_test() throws Exception{

    }

    @Test
    @Transactional
    @DisplayName("배너 게시글 검색 테스트")
    public void banner_post_search_test() throws Exception{

        List<BannerResponseDto> banners = postService.getBannerPost();

        System.out.println(banners);
    }


}