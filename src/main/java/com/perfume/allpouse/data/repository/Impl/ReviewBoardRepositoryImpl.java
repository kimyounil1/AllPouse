package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.entity.QPost;
import com.perfume.allpouse.data.entity.QReviewBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.repository.custom.ReviewBoardRepositoryCustom;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.QReviewResponseDto;
import com.perfume.allpouse.model.dto.QSearchReviewDto;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SearchReviewDto;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.utils.PageUtils;
import com.perfume.allpouse.utils.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.perfume.allpouse.data.entity.QReviewBoard.reviewBoard;
import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.exception.ExceptionEnum.REVIEW_ID_NOT_FOUND;
import static com.perfume.allpouse.model.enums.BoardType.REVIEW;
import static com.perfume.allpouse.model.enums.BoardType.USER;

public class ReviewBoardRepositoryImpl extends QuerydslRepositorySupport implements ReviewBoardRepositoryCustom {

    public ReviewBoardRepositoryImpl() {
        super(ReviewBoard.class);
    }

    QReviewBoard review = reviewBoard;

    QPhoto reviewPhoto = new QPhoto("reviewPhoto");
    QPhoto userPhoto = new QPhoto("userPhoto");


    // 기본검색
    @Override
    public List<ReviewResponseDto> search(String keyword) {

        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(review.subject.containsIgnoreCase(keyword).or(review.content.containsIgnoreCase(keyword)))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(review.subject.asc())
                .limit(10)
                .fetch();

        return reviewDtoList;
    }


    // 기본검색 -> 전체 리뷰
    @Override
    public Page<ReviewResponseDto> searchWithPaging(String keyword, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(review.subject.containsIgnoreCase(keyword).or(review.content.containsIgnoreCase(keyword)))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<ReviewResponseDto> pages = PageUtils.makePageList(reviewDtoList, pageable);

        return pages;

    }


    // 유저작성 리뷰(+ 페이지네이션)
    @Override
    public Page<ReviewResponseDto> getReviewDto(Long userId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(review.user.id.eq(userId))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<ReviewResponseDto> pages = PageUtils.makePageList(reviewDtoList, pageable);

        return pages;
    }


    // 전체 리뷰 DTO
    @Override
    public Page<ReviewResponseDto> getRecentReviewDto(Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<ReviewResponseDto> pages = PageUtils.makePageList(reviewDtoList, pageable);

        return pages;
    }


    // 리뷰 id로 리뷰 검색해서 ReviewResponseDto 가져옴
    @Override
    public ReviewResponseDto getReviewDtoByReviewId(Long reviewId) {

        try{
            List<ReviewResponseDto> reviewDtoList = from(review)
                    .leftJoin(reviewPhoto)
                    .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                    .leftJoin(userPhoto)
                    .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                    .where(review.id.eq(reviewId))
                    .select(new QReviewResponseDto(
                            review.id,
                            review.user.id,
                            userPhoto.path,
                            review.user.userName,
                            review.subject,
                            review.content,
                            review.perfume.subject,
                            review.perfume.brand.name,
                            review.hitCnt,
                            review.recommendCnt,
                            reviewPhoto.path,
                            review.createDateTime))
                    .fetch();

            if (reviewDtoList.size() != 1) {
                throw new CustomException(REVIEW_ID_NOT_FOUND);
            }

            return reviewDtoList.get(0);

        } catch(Exception e) {
                throw new CustomException(INVALID_PARAMETER); }
    }


    // 브랜드에 달린 리뷰 DTO
    @Override
    public List<ReviewResponseDto> getReviewsOnBrand(Long brandId, Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(review.perfume.brand.id.eq(brandId))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        return reviewDtoList;
    }


    // 브랜드에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 리뷰 DTO
    @Override
    public List<ReviewResponseDto> getReviewsOnBrand(Long brandId, Permission permission, int size) {

        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(review.perfume.brand.id.eq(brandId).and(review.user.permission.eq(permission)))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(review.hitCnt.desc())
                .limit(size)
                .fetch();

        return reviewDtoList;
    }

    // 향수에 달린 리뷰 DTO
    @Override
    public List<ReviewResponseDto> getReviewsOnPerfume(Long perfumeId, Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(review.perfume.id.eq(perfumeId))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .limit(3)
                .fetch();

        return reviewDtoList;
    }


    // 향수에 달린 사용자분류(USER : 일반사용자, PERFUMER : 조향사)별 베스트 리뷰 DTO
    @Override
    public List<ReviewResponseDto> getReviewsOnPerfume(Long perfumeId, Permission permission, int size) {
        List<ReviewResponseDto> reviewDtoList = from(review)
                .leftJoin(reviewPhoto)
                .on(review.id.eq(reviewPhoto.boardId).and(reviewPhoto.boardType.eq(REVIEW)))
                .leftJoin(userPhoto)
                .on(review.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(review.perfume.id.eq(perfumeId).and(review.user.permission.eq(permission)))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.id,
                        userPhoto.path,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        reviewPhoto.path,
                        review.createDateTime))
                .orderBy(review.hitCnt.desc())
                .limit(size)
                .fetch();

        return reviewDtoList;
    }


    // Pageable에서 정렬기준 추출
    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = Order.DESC;


                // order.getProperty() : recommendCnt, createDateTime
                switch(order.getProperty()) {

                    // 추천 순
                    case "recommendCnt":
                        OrderSpecifier<?> orderName = QueryDslUtil.getSortedColumn(direction, reviewBoard.recommendCnt, "recommendCnt");
                        ORDERS.add(orderName);
                        break;

                    // 작성일 순
                    case "createDateTime":
                        OrderSpecifier<?> orderDateTime = QueryDslUtil.getSortedColumn(direction, reviewBoard.createDateTime, "createDateTime");
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