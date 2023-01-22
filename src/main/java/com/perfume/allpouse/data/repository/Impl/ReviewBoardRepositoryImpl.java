package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.entity.QPost;
import com.perfume.allpouse.data.entity.QReviewBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.repository.custom.ReviewBoardRepositoryCustom;
import com.perfume.allpouse.model.dto.QReviewResponseDto;
import com.perfume.allpouse.model.dto.QSearchReviewDto;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SearchReviewDto;
import com.perfume.allpouse.utils.PageUtils;
import com.perfume.allpouse.utils.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.REVIEW;

public class ReviewBoardRepositoryImpl extends QuerydslRepositorySupport implements ReviewBoardRepositoryCustom {

    public ReviewBoardRepositoryImpl() {
        super(ReviewBoard.class);
    }

    QReviewBoard review = QReviewBoard.reviewBoard;
    QPhoto photo = QPhoto.photo;


    // 기본검색
    @Override
    public List<ReviewResponseDto> search(String keyword) {

        List<ReviewResponseDto> reviews = from(review)
                .leftJoin(photo)
                .on(review.id.eq(photo.boardId).and(photo.boardType.eq(REVIEW)))
                .where(review.subject.containsIgnoreCase(keyword).or(review.content.containsIgnoreCase(keyword)))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        photo.path,
                        review.createDateTime))
                .orderBy(review.subject.asc())
                .limit(10)
                .fetch();



        return reviews;
    }


    // 기본검색 -> 전체 리뷰
    @Override
    public Page<ReviewResponseDto> searchWithPaging(String keyword, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ReviewResponseDto> reviews = from(review)
                .leftJoin(photo)
                .on(review.id.eq(photo.boardId).and(photo.boardType.eq(REVIEW)))
                .where(review.subject.containsIgnoreCase(keyword).or(review.content.containsIgnoreCase(keyword)))
                .select(new QReviewResponseDto(
                        review.id,
                        review.user.userName,
                        review.subject,
                        review.content,
                        review.perfume.subject,
                        review.perfume.brand.name,
                        review.hitCnt,
                        review.recommendCnt,
                        photo.path,
                        review.createDateTime))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<ReviewResponseDto> pages = PageUtils.makePageList(reviews, pageable);

        return pages;

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