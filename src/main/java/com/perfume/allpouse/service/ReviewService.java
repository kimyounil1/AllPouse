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


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewBoardRepository reviewRepository;
    private final UserRepository userRepository;
    private final PerfumeBoardRepository perfumeRepository;

    // 리뷰 저장
    @Transactional
    public Long save(SaveReviewDto reviewDto) {

        ReviewBoard review = toEntity(reviewDto);
        ReviewBoard savedReview = reviewRepository.save(review);

        return savedReview.getId();
    }

    // 리뷰 수정
    @Transactional
    public Long update(SaveReviewDto reviewDto) {

        ReviewBoard review = reviewRepository.findById(reviewDto.getId()).get();
        review.changeReview(reviewDto);

        return review.getId();
    }


    // 향수에 대한 리뷰 전체 조회
    // id : PerfumeBoard id
    //public List<ReviewBoard> findAll(Long id) {
    //
//
    //}


    private ReviewBoard toEntity(SaveReviewDto reviewDto) {
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
