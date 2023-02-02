package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.*;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.ReviewService;
import com.perfume.allpouse.utils.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.exception.ExceptionEnum.REVIEW_ID_NOT_FOUND;
import static com.perfume.allpouse.model.enums.BoardType.REVIEW;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewBoardRepository reviewRepository;

    private final PerfumeBoardRepository perfumeRepository;

    private final UserRepository userRepository;

    private final PhotoService photoService;

    private final PostRepository postRepository;

    private final EntityManager em;

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final JPAQueryFactory queryFactory;

    QReviewBoard reviewBoard = new QReviewBoard("reviewBoard");
    QPhoto photo = new QPhoto("photo");


    // 리뷰 저장 - 사진 있는 경우
    @Override
    @Transactional
    public Long save(SaveReviewDto saveReviewDto, List<MultipartFile> photos) throws IOException {

        Long reviewId = saveReviewDto.getId();
        PerfumeBoard perfume = perfumeRepository.findById(saveReviewDto.getPerfumeId()).get();

        // 등록된 적 없는 리뷰 -> 글/사진 저장
        if (reviewId == null) {
            ReviewBoard savedReview = reviewRepository.save(toEntity(saveReviewDto));
            perfume.updateScore_save(saveReviewDto.getScore());
            Long savedId = savedReview.getId();
            List<String> save = photoService.save(photos, REVIEW, savedId);
            System.out.println(save);
            return savedId;
        }

        // 등록된 적 있는 리뷰 -> 사진 삭제 후 저장, 리뷰 내용 변경
        else {
            photoService.delete(REVIEW, reviewId);
            photoService.save(photos, REVIEW, reviewId);
            update(saveReviewDto);
            return reviewId;
        }
    }


    // 리뷰 저장 - 사진 없는 경우
    @Transactional
    @Override
    public Long save(SaveReviewDto saveReviewDto) {

        Long reviewId = saveReviewDto.getId();
        PerfumeBoard perfume = perfumeRepository.findById(saveReviewDto.getPerfumeId()).get();

        // 아직 저장된 적 없음 -> save
        if (reviewId == null) {
            ReviewBoard savedReview = reviewRepository.save(toEntity(saveReviewDto));

            perfume.updateScore_save(saveReviewDto.getScore());
            return savedReview.getId();
        } else {
            photoService.delete(REVIEW, reviewId);
            Long savedReviewId = update(saveReviewDto);
            return savedReviewId;
        }
    }


    // 리뷰 수정
    @Transactional
    @Override
    public Long update(SaveReviewDto saveReviewDto) {

        Long reviewId = saveReviewDto.getId();

        Optional<ReviewBoard> review = reviewRepository.findById(reviewId);

        if (review.isPresent()) {
            ReviewBoard prevReviewBoard = review.get();
            int newScore = saveReviewDto.getScore();

            Long perfumeId = prevReviewBoard.getPerfume().getId();
            PerfumeBoard perfumeBoard = perfumeRepository.findById(perfumeId).get();

            // 리뷰 점수 -> 향수에 업데이트 반영
            perfumeBoard.updateScore_update(prevReviewBoard, newScore);

            // 리뷰 내용 수정
            prevReviewBoard.changeReview(saveReviewDto);

            return reviewId;
        } else {throw new CustomException(INVALID_PARAMETER);}
    }


    // 리뷰 삭제
    @Transactional
    @Override
    public void delete(Long id) {

        Optional<ReviewBoard> review = reviewRepository.findById(id);

        if (review.isPresent()) {
            ReviewBoard reviewBoard = review.get();
            PerfumeBoard perfumeBoard = perfumeRepository.findById(reviewBoard.getPerfume().getId()).get();

            // 향수 점수 수정 + 향수-리뷰 연관관계 끊음
            perfumeBoard.updateScore_delete(reviewBoard);

            // 리뷰 삭제
            reviewRepository.deleteById(id);

        } else {
            throw new IllegalStateException("삭제할 리뷰가 없습니다.");
        }
    }


    /**
     * 리뷰 조회 메소드
     */
    //회원이 작성한 전체 리뷰 조회(파라미터 user_id)
    //기본정렬 : 작성일자(cre_dt)
    @Override
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
    @Override
    public List<ReviewBoard> findByPerfumeId(Long id) {

        List<ReviewBoard> reviews = reviewRepository.findReviewsByPerfumeId(id);

        if (reviews.size() == 0) {
            throw new IllegalStateException("해당 향수에 대한 리뷰가 없습니다.");
        } else {
            return reviews;
        }

    }

    // 리뷰 id로 조회
    @Override
    public ReviewBoard findById(Long id) {
        Optional<ReviewBoard> review = reviewRepository.findById(id);

        if (review.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 리뷰입니다.");
        } else {
            return review.get();
        }

    }


    // 유저가 작성한 리뷰를 ResponseDto 형식으로 페이지네이션해서 가져옴
    // 기본정렬 : 작성일자 기준 내림차순(pageable로 변경 가능)
    @Override
    public Page<ReviewResponseDto> getReviewDto(Long userId, Pageable pageable) {
        Page<ReviewResponseDto> pages = reviewRepository.getReviewDto(userId, pageable);

        return pages;
    }


    // 전체 리뷰와 사진을 ReviewResponseDto로 변환해서 가져옴
    // 정렬 : 작성일자 기준 내림차순(고정)
    @Override
    public Page<ReviewResponseDto> getRecentReviewDto(Pageable pageable) {
        Page<ReviewResponseDto> pages = reviewRepository.getRecentReviewDto(pageable);

        return pages;
    }


    // 리뷰 id로 리뷰 검색해서 반환
    // 리뷰 상세 페이지 전달할 때 사용 -> 조회수 + 1 로직 포함
    @Override
    @Transactional
    public ReviewResponseDto getReviewDtoByReviewId(Long reviewId) {

        ReviewResponseDto reviewDto = reviewRepository.getReviewDtoByReviewId(reviewId);

        // 조회수 + 1
        reviewRepository.updateHitCnt(reviewId);
        return reviewDto;
    }


    // 브랜드에 달린 리뷰를 가져와서 반환
    // 기본정렬 : 작성일자 기준 내림차순
    @Override
    public List<ReviewResponseDto> getReviewsOnBrand(Long brandId, Pageable pageable) {
        List<ReviewResponseDto> reviewDtoList = reviewRepository.getReviewsOnBrand(brandId, pageable);

        return reviewDtoList;
    }


    // 브랜드에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 리뷰 가져와서 반환
    // 정렬 : 추천수 기준 내림차순(고정)
    @Override
    public List<ReviewResponseDto> getReviewsOnBrand(Long brandId, Permission permission, int size) {

        List<ReviewResponseDto> reviewDtoList = reviewRepository.getReviewsOnBrand(brandId, permission, size);


        return reviewDtoList;
    }


    // 향수에 달린 리뷰들 가져와서 반환
    // 기본정렬 : 작성일자 기준 내림차순
    @Override
    public List<ReviewResponseDto> getReviewsOnPerfume(Long perfumeId, Pageable pageable) {

        List<ReviewResponseDto> reviewDtoList = reviewRepository.getReviewsOnPerfume(perfumeId, pageable);

        return reviewDtoList;
    }


    // 향수에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 댓글 가져와서 반환
    // 정렬 : 추천수 기준 내림차순(고정)
    @Override
    public List<ReviewResponseDto>getReviewsOnPerfume(Long perfumeId, Permission permission, int size) {

        List<ReviewResponseDto> reviewDtoList = reviewRepository.getReviewsOnPerfume(perfumeId, permission, size);

        return reviewDtoList;
    }


    // 유저가 리뷰 추천했는지 여부
    @Override
    public boolean isRecommended(Long reviewId, Long userId) {

        ReviewBoard review = reviewRepository.findById(reviewId).get();
        List<Long> userList = review.getRecommendUserList();

        return userList.contains(userId);
    }


    // 리뷰 추천 기능
    // 처음 추천 : 0 / 이미 추천한 게시물 : 1
    @Override
    @Transactional
    public int updateRecommendCnt(Long reviewId, Long userId) {

        Optional<ReviewBoard> reviewOpt = reviewRepository.findById(reviewId);

        if (reviewOpt.isEmpty()) {
            throw new CustomException(REVIEW_ID_NOT_FOUND);
        }
        // 리뷰 존재
        else {
            ReviewBoard review = reviewOpt.get();
            List<Long> userList = review.getRecommendUserList();

            // 해당 게시물 추천한 사람 없거나, 유저가 게시글 추천한 적 X -> 0 (추천)
            if (userList == null || !userList.contains(userId)) {
                userList.add(userId);
                reviewRepository.addRecommendCnt(reviewId);
                return 0;
            }
            // 추천한 적 O -> 1 (추천취소)
            else {
                userList.remove(userId);
                reviewRepository.minusRecommendCnt(reviewId);
                return 1;
            }
        }
    }


    // DTO -> ReviewBoard
    private ReviewBoard toEntity(SaveReviewDto reviewDto) {

        //Optional 예외처리 필요
        User user = userRepository.findById(reviewDto.getUserId()).get();
        PerfumeBoard perfume = perfumeRepository.findById(reviewDto.getPerfumeId()).get();

        ReviewBoard review = ReviewBoard.builder()
                .id(reviewDto.getId())
                .subject(reviewDto.getSubject())
                .content(reviewDto.getContent())
                .score(reviewDto.getScore())
                .build();

        addUserAndPerfume(review, user, perfume);

        return review;
    }


    private void addUserAndPerfume(ReviewBoard reviewBoard, User user, PerfumeBoard perfumeBoard) {
        reviewBoard.setUser(user);
        reviewBoard.setPerfume(perfumeBoard);
    }


    // Pageable에서 정렬기준 추출
    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;


                // order.getProperty() : recommendCnt, createDateTime
                switch(order.getProperty()) {

                    // 추천 순
                    case "recommendCnt":
                        OrderSpecifier<?> orderName = QueryDslUtil.getSortedColumn(direction, QReviewBoard.reviewBoard.recommendCnt, "recommendCnt");
                        ORDERS.add(orderName);
                        break;

                    // 작성일 순
                    case "createDateTime":
                        OrderSpecifier<?> orderDateTime = QueryDslUtil.getSortedColumn(direction, QReviewBoard.reviewBoard.createDateTime, "createDateTime");
                        ORDERS.add(orderDateTime);
                        break;

                    default:
                        break;
                }
            }
        }
        return ORDERS;
    }

}
