package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
class ReviewServiceImplTest {

    @Autowired
    ReviewBoardRepository reviewRepository;

    @Autowired
    ReviewServiceImpl reviewService;

    @Test
    @DisplayName("리뷰 응답 DTO 테스트")
    @Transactional
    public void reviewResponseDtoTest() throws Exception{

        List<ReviewResponseDto> result = reviewService.getReviewDto(5L);
        System.out.println(result);

    }

    @Test
    @DisplayName("향수 id, 권한으로 리뷰 찾기")
    @Transactional
    public void findReviewsWithPerfumeIdAndPermission() throws Exception {
        //given
        List<ReviewResponseDto> result = reviewService.getReviewDtoByPerfumeId(9L);


        System.out.println(result);
    }
}