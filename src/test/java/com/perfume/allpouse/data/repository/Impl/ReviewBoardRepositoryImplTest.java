package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.model.dto.SearchReviewDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReviewBoardRepositoryImplTest {

    @Autowired
    ReviewBoardRepository reviewBoardRepository;

    @Test
    @DisplayName("리뷰 기본 검색 테스트")
    public void basic_review_search_test() throws Exception{

        //given
        String keyword = "new";

        //when
        List<SearchReviewDto> search = reviewBoardRepository.search(keyword);

        //then
        Assertions.assertThat(search).isNotNull();
    }
}