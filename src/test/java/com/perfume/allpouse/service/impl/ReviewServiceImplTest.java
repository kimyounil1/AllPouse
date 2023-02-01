package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.Photo;
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

import java.math.BigDecimal;
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
        //given
        Long userId = 1381L;
        Long perfumeId = 134L;

        for(int i = 1; i <= 10; i++){

            SaveReviewDto dto = new SaveReviewDto("test subject", "test content", i, userId, perfumeId);

            Long savedId = reviewService.save(dto);

            PerfumeBoard perfume = perfumeRepository.findById(perfumeId).get();

            BigDecimal score = perfume.getScore();
            int reviewSize = perfume.getReviews().size();

            System.out.println(i + "번째 리뷰 추가 후 리뷰 개수  : " + reviewSize);
            System.out.println(i + "번째 리뷰 추가 후 향수 스코어 : " + score);
        }
        //when
        
        //then
        
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

}