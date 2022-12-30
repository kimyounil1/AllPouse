package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PostRepositoryImplTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 검색 테스트")
    public void basic_post_search_test() throws Exception{
        //given
        String keyword = "테스트";

        //when
        List<SearchPostDto> search = postRepository.search(keyword);

        //then
        Assertions.assertThat(search).isNotNull();
    }

}