package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.*;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.QReviewResponseDto;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.model.enums.BoardType.PERFUME;
import static com.perfume.allpouse.model.enums.BoardType.REVIEW;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewBoardRepository reviewRepository;

    private final PerfumeBoardRepository perfumeRepository;

    private final UserRepository userRepository;

    private final PhotoService photoService;

    private final Logger LOGGER = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final JPAQueryFactory queryFactory;

    QReviewBoard reviewBoard = new QReviewBoard("reviewBoard");
    QPhoto photo = new QPhoto("photo");


    // 리뷰 저장
    @Transactional
    @Override
    public Long save(SaveReviewDto saveReviewDto) {

        ReviewBoard reviewBoard = toEntity(saveReviewDto);
        ReviewBoard savedReview = reviewRepository.save(reviewBoard);

        return savedReview.getId();
    }


    // 리뷰 수정
    @Transactional
    @Override
    public Long update(SaveReviewDto saveReviewDto) {

        ReviewBoard review = reviewRepository.findById(saveReviewDto.getId()).get();
        review.changeReview(saveReviewDto);

        return review.getId();
    }


    // 리뷰 삭제
    @Transactional
    @Override
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


    // Dto -> ReviewBoard
    private ReviewBoard toEntity(SaveReviewDto reviewDto) {

        //Optional 예외처리 필요
        User user = userRepository.findById(reviewDto.getUserId()).get();
        PerfumeBoard perfume = perfumeRepository.findById(reviewDto.getPerfumeId()).get();

        ReviewBoard review = ReviewBoard.builder()
                .id(reviewDto.getId())
                .subject(reviewDto.getSubject())
                .content(reviewDto.getContent())
                .build();

        addUserAndPerfume(review, user, perfume);

        return review;
    }

    private void addUserAndPerfume(ReviewBoard reviewBoard, User user, PerfumeBoard perfumeBoard) {
        reviewBoard.setUser(user);
        reviewBoard.setPerfume(perfumeBoard);
    }



    // 유저가 작성한 리뷰와 사진 List<ReviewResponseDto>로 변환해서 가져옴
    // 기본정렬 : 작성일자 기준 내림차순(pageable로 변경 가능)
    @Override
    public Page<ReviewResponseDto> getReviewDto(Long userId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = queryFactory
                .select(new QReviewResponseDto(reviewBoard.id, reviewBoard.user.userName, reviewBoard.subject, reviewBoard.content, reviewBoard.perfume.subject, reviewBoard.perfume.brand.name, reviewBoard.hitCnt, reviewBoard.recommendCnt, photo.path, reviewBoard.createDateTime))
                .from(reviewBoard)
                .leftJoin(photo)
                .on(reviewBoard.id.eq(photo.boardId).and(photo.boardType.eq(REVIEW)))
                .where(reviewBoard.user.id.eq(userId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewDtoList.size());
        Page<ReviewResponseDto> pageList = new PageImpl<>(reviewDtoList.subList(start, end), pageable, reviewDtoList.size());

        return pageList;
    }


    // 전체 리뷰와 사진을 ReviewResponseDto로 변환해서 가져옴
    // 정렬 : 작성일자 기준 내림차순(고정)
    @Override
    public Page<ReviewResponseDto> getRecentReviewDto(Pageable pageable) {

        List<ReviewResponseDto> reviewDtoList = queryFactory
                .select(new QReviewResponseDto(reviewBoard.id, reviewBoard.user.userName, reviewBoard.subject, reviewBoard.content, reviewBoard.perfume.subject, reviewBoard.perfume.brand.name, reviewBoard.hitCnt, reviewBoard.recommendCnt, photo.path, reviewBoard.createDateTime))
                .from(reviewBoard)
                .leftJoin(photo)
                .on(reviewBoard.id.eq(photo.boardId).and(photo.boardType.eq(REVIEW)))
                .orderBy(reviewBoard.createDateTime.desc())
                .fetch();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewDtoList.size());
        Page<ReviewResponseDto> pageList = new PageImpl<>(reviewDtoList.subList(start, end), pageable, reviewDtoList.size());

        return pageList;
    }


    // 리뷰 id로 리뷰 검색해서 반환
    @Override
    public ReviewResponseDto getReviewDtoByReviewId(Long reviewId) {

        try{
            List<ReviewResponseDto> reviewDtoList = queryFactory
                    .select(new QReviewResponseDto(reviewBoard.id, reviewBoard.user.userName, reviewBoard.subject, reviewBoard.content, reviewBoard.perfume.subject, reviewBoard.perfume.brand.name, reviewBoard.hitCnt, reviewBoard.recommendCnt, photo.path, reviewBoard.createDateTime))
                    .from(reviewBoard)
                    .leftJoin(photo)
                    .on(reviewBoard.id.eq(photo.boardId).and(photo.boardType.eq(REVIEW)))
                    .where(reviewBoard.id.eq(reviewId))
                    .fetch();

            if (reviewDtoList.size() != 1) {throw new CustomException(INVALID_PARAMETER);}

            return reviewDtoList.get(0);

        } catch (Exception e) {throw new CustomException(INVALID_PARAMETER);}
    }


    // 브랜드에 달린 리뷰를 가져와서 반환
    // 기본정렬 : 작성일자 기준 내림차순
    @Override
    public List<ReviewResponseDto> getReviewsOnBrand(Long boardId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = queryFactory
                .select(new QReviewResponseDto(reviewBoard.id, reviewBoard.user.userName, reviewBoard.subject, reviewBoard.content, reviewBoard.perfume.subject, reviewBoard.perfume.brand.name, reviewBoard.hitCnt, reviewBoard.recommendCnt, photo.path, reviewBoard.createDateTime))
                .from(reviewBoard)
                .leftJoin(photo)
                .on(reviewBoard.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                .where(reviewBoard.perfume.brand.id.eq(boardId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        return reviewDtoList;
    }

    // 브랜드에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 댓글 가져와서 반환
    // 정렬 : 추천수 기준 내림차순(고정)
    @Override
    public List<ReviewResponseDto> getReviewsOnBrand(Long boardId, Permission permission, int size) {

        List<ReviewResponseDto> reviewDtoList = queryFactory
                .select(new QReviewResponseDto(reviewBoard.id, reviewBoard.user.userName, reviewBoard.subject, reviewBoard.content, reviewBoard.perfume.subject, reviewBoard.perfume.brand.name, reviewBoard.hitCnt, reviewBoard.recommendCnt, photo.path, reviewBoard.createDateTime))
                .from(reviewBoard)
                .leftJoin(photo)
                .on(reviewBoard.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                .where(reviewBoard.perfume.brand.id.eq(boardId).and(reviewBoard.user.permission.eq(permission)))
                .orderBy(reviewBoard.hitCnt.desc())
                .limit(size)
                .fetch();

        return reviewDtoList;
    }


    // 향수에 달린 리뷰들 가져와서 반환
    // 기본정렬 : 작성일자 기준 내림차순
    @Override
    public List<ReviewResponseDto> getReviewsOnPerfume(Long boardId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = queryFactory
                .select(new QReviewResponseDto(reviewBoard.id, reviewBoard.user.userName, reviewBoard.subject, reviewBoard.content, reviewBoard.perfume.subject, reviewBoard.perfume.brand.name, reviewBoard.hitCnt, reviewBoard.recommendCnt, photo.path, reviewBoard.createDateTime))
                .from(reviewBoard)
                .leftJoin(photo)
                .on(reviewBoard.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                .where(reviewBoard.perfume.id.eq(boardId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        return reviewDtoList;
    }


    // 향수에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 댓글 가져와서 반환
    // 정렬 : 추천수 기준 내림차순(고정)
    @Override
    public List<ReviewResponseDto>getReviewsOnPerfume(Long boardId, Permission permission, int size) {

        List<ReviewResponseDto> reviewDtoList = queryFactory
                .select(new QReviewResponseDto(reviewBoard.id, reviewBoard.user.userName, reviewBoard.subject, reviewBoard.content, reviewBoard.perfume.subject, reviewBoard.perfume.brand.name, reviewBoard.hitCnt, reviewBoard.recommendCnt, photo.path, reviewBoard.createDateTime))
                .from(reviewBoard)
                .leftJoin(photo)
                .on(reviewBoard.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                .where(reviewBoard.perfume.id.eq(boardId).and(reviewBoard.user.permission.eq(permission)))
                .orderBy(reviewBoard.hitCnt.desc())
                .limit(size)
                .fetch();

        return reviewDtoList;
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
