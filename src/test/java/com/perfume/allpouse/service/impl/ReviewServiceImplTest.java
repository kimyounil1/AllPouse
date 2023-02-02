package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.data.repository.PhotoRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.ReviewService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Autowired
    PerfumeBoardRepository perfumeRepository;


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
    
    
    @Test
    @DisplayName("리뷰 스코어 향수 반영 테스트 1")
    @Transactional
    public void reviewScoreReflectionTest_1() throws Exception{

        Long perfumeId = 2781L;
        Long userId = 5L;

        SaveReviewDto dto_1 = new SaveReviewDto("test subject", "content", 10, userId, perfumeId);
        SaveReviewDto dto_2 = new SaveReviewDto("test subject", "content", 3, userId, perfumeId);


        Long oneId = reviewService.save(dto_1);
        Long twoId = reviewService.save(dto_2);


        ReviewBoard review_1 = reviewRepository.findById(oneId).get();
        ReviewBoard review_2 = reviewRepository.findById(twoId).get();


        System.out.println("review_score : " + review_1.getScore());
        Assertions.assertThat(review_2.getScore()).isNotNull();

        PerfumeBoard perfume = perfumeRepository.findById(perfumeId).get();
        System.out.println("perfume_score : " + perfume.getScore());
        Assertions.assertThat(perfume.getScore()).isNotZero();
    }

    @Test
    @Transactional
    @DisplayName("리뷰 스코어 설정 테스트")
    public void setReviewScoreTest() throws Exception{
        //given
        PerfumeBoard.builder();
        Long userId = 5l;
        Long perfumeId = 134L;

        SaveReviewDto dto = new SaveReviewDto("subject", "content", 5, userId, perfumeId);

        //when
        reviewService.save(dto);

        //then

    }

    @Test
    @Transactional
    @DisplayName("리뷰 생성 테스트 - 사진 없음")
    public void saveReviewTest_1() throws Exception{
        //given
        SaveReviewDto saveReviewDto = new SaveReviewDto("테스트 주제", "테스트 컨텐츠", 4, 5L, 132L);

        //when
        Long savedId = reviewService.save(saveReviewDto);

        //then
        Assertions.assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("리뷰 삭제 -> Perfume.reviews 반영 테스트")
    @Transactional
    public void delete_test_1() throws Exception{
        //given
        Long reviewId = 2802L;
        Long perfumeId = 2800L;

        reviewService.delete(reviewId);

        PerfumeBoard perfume = perfumeRepository.findById(perfumeId).get();

        Assertions.assertThat(perfume.getReviews().size()).isEqualTo(0);
        Assertions.assertThat(perfume.getScore()).isEqualTo(0);
    }

    @Test
    @DisplayName("리뷰 업데이트 -> Perfume.score 반영 테스트" +
            "Perfume.reviews 1개일 때")
    @Transactional
    public void update_test_1() throws Exception{
        //given
        Long reviewId = 2802L;
        Long perfumeId = 2800L;

        //when
        SaveReviewDto dto = new SaveReviewDto(reviewId, "changed subject", "changed content", 5, 5L, 2800L);
        reviewService.save(dto);

        //then
        ReviewBoard review = reviewRepository.findById(reviewId).get();
        PerfumeBoard perfume = perfumeRepository.findById(perfumeId).get();

        int reviewSize = perfume.getReviews().size();

        Assertions.assertThat(reviewSize).isEqualTo(1);
        Assertions.assertThat(review.getScore()).isEqualTo(5);
        Assertions.assertThat(perfume.getScore()).isEqualTo(5);
    }



}