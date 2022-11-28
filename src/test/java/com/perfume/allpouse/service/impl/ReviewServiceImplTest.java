package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
class ReviewServiceImplTest {

    @Autowired
    ReviewServiceImpl reviewService;

    @Test
    @DisplayName("리뷰 응답 DTO 테스트")
    @Transactional
    public void reviewResponseDtoTest() throws Exception{

        List<ReviewResponseDto> result = reviewService.getReviewDto(5L);
        System.out.println(result);

    }
}