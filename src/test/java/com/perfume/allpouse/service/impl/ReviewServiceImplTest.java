package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Photo;
import com.perfume.allpouse.data.repository.PhotoRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.ReviewService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.REVIEW;


@SpringBootTest
class ReviewServiceImplTest {

    @Autowired
    ReviewBoardRepository reviewRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PhotoService photoService;


    @Test
    @DisplayName("리뷰 응답 DTO 테스트")
    @Transactional
    public void reviewResponseDtoTest() throws Exception{

    }

    @Test
    @DisplayName("향수 id, 권한으로 리뷰 찾기")
    @Transactional
    public void findReviewsWithPerfumeIdAndPermission() throws Exception {

    }

    @Test
    @DisplayName("리뷰 삭제 - S3 객체 삭제 테스트")
    @Transactional
    @Rollback(false)
    public void s3ObejectDeleteTest() throws Exception{
        //given
        Long reviewId = 516L;
        //Photo photo = photoRepository.findPhotoByBoardTypeAndBoardId(REVIEW, reviewId);

        photoService.delete(REVIEW, reviewId);
    }

    @Test
    @DisplayName("리뷰 id로 리뷰 찾기 테스트")
    @Transactional
    public void findReviewByReviewId() throws Exception{
        //given
        Long reviewId = 1425L;

        //when
        ReviewResponseDto dto = reviewRepository.getReviewDtoByReviewId(reviewId);

        //then
        Assertions.assertThat(dto.getId()).isEqualTo(reviewId);
    }

}