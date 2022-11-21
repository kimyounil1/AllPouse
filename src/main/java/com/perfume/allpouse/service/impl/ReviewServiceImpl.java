package com.perfume.allpouse.service.impl;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.service.ReviewService;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewBoardRepository reviewRepository;
    private final PerfumeBoardRepository perfumeRepository;
    private final UserRepository userRepository;


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


    /**
     * 리뷰 조회 메소드
     */

    //회원이 작성한 전체 리뷰 조회(파라미터 user_id)
    //기본정렬 : 작성일자(cre_dt)
    public List<ReviewBoard> findByUserId(Long id) {

        List<ReviewBoard> reviews = reviewRepository.findReviewsByUserId(id);

        if (reviews.size() == 0) {
            throw new IllegalStateException("해당 유저가 작성한 리뷰가 없습니다.");
        } else {
            return reviews;
        }
    }


    //향수에 대한 리뷰 전체 조회(파라미터 perfumeBoard_id)
    //기본정렬 : 작성일자(cre_dt)
    public List<ReviewBoard> findByPerfumeId(Long id) {

        List<ReviewBoard> reviews = reviewRepository.findReviewsByPerfumeId(id);

        if (reviews.size() == 0) {
            throw new IllegalStateException("해당 향수에 대한 리뷰가 없습니다.");
        } else {
            return reviews;
        }

    }



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
