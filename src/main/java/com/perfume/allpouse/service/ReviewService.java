package com.perfume.allpouse.service;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.repository.PerfumeBoardRepository;
import com.perfume.allpouse.repository.ReviewBoardRepository;
import com.perfume.allpouse.repository.UserRepository;
import com.perfume.allpouse.service.dto.SaveReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewBoardRepository reviewRepository;
    private final UserRepository userRepository;
    private final PerfumeBoardRepository perfumeRepository;


    // 리뷰 저장
    @Transactional
    public Long save(SaveReviewDto saveReviewDto) {

        ReviewBoard reviewBoard = toEntity(saveReviewDto);
        ReviewBoard savedReview = reviewRepository.save(reviewBoard);

        return savedReview.getId();
    }


    // 리뷰 수정
    @Transactional
    public Long update(SaveReviewDto saveReviewDto) {

        ReviewBoard review = reviewRepository.findById(saveReviewDto.getId()).get();
        review.changeReview(saveReviewDto);

        return review.getId();
    }


    // 리뷰 삭제
    @Transactional
    public void delete(Long id) {

        Optional<ReviewBoard> review = reviewRepository.findById(id);

        if (review.isPresent()) {
            reviewRepository.delete(review.get());
        } else {
            throw new IllegalStateException("삭제할 리뷰가 없습니다.");
        }
    }





    // 향수에 대한 리뷰 전체 조회
    // id : PerfumeBoard id
    //public List<ReviewBoard> findAll(Long id) {
    //
//
    //}



    // Dto -> ReviewBoard
    private ReviewBoard toEntity(SaveReviewDto reviewDto) {

        //Optional 예외처리 필요
        User user = userRepository.findById(reviewDto.getUserId()).get();
        PerfumeBoard perfume = perfumeRepository.findById(reviewDto.getPerfumeId()).get();

        ReviewBoard review = ReviewBoard.builder()
                .id(reviewDto.getId())
                .subject(reviewDto.getSubject())
                .content(reviewDto.getContent())
                .imagePath(reviewDto.getImagePath())
                .build();

        addUserAndPerfume(review, user, perfume);

        return review;
    }

    private void addUserAndPerfume(ReviewBoard reviewBoard, User user, PerfumeBoard perfumeBoard) {
        reviewBoard.setUser(user);
        reviewBoard.setPerfume(perfumeBoard);
    }

}
