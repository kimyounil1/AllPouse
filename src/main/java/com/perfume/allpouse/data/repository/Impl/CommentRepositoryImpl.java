package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.entity.QComment;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.custom.CommentRepositoryCustom;
import com.perfume.allpouse.model.dto.CommentResponseDto;
import com.perfume.allpouse.model.dto.QCommentResponseDto;
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

import static com.perfume.allpouse.model.enums.BoardType.USER;

public class CommentRepositoryImpl extends QuerydslRepositorySupport implements CommentRepositoryCustom {

    public CommentRepositoryImpl() {super(Comment.class);}

    QComment comment = QComment.comment;
    QPhoto photo = QPhoto.photo;


    // 유저가 작성한 리뷰 댓글
    @Override
    public Page<CommentResponseDto> getUserCommentDtoList(Long userId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<CommentResponseDto> dtoList = from(comment)
                .leftJoin(photo)
                .on(comment.user.id.eq(photo.boardId).and(photo.boardType.eq(USER)))
                .where(comment.user.id.eq(userId))
                .select(new QCommentResponseDto(
                        comment.id,
                        comment.title,
                        comment.content,
                        comment.user.id,
                        comment.user.userName,
                        photo.path,
                        comment.review.id,
                        comment.review.perfume.subject,
                        comment.createDateTime
                ))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<CommentResponseDto> page = PageUtils.makePageList(dtoList, pageable);

        return page;
    }


    // 모든 댓글(리뷰댓글)
    @Override
    public Page<CommentResponseDto> getAllCommentDtoList(Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<CommentResponseDto> dtoList = from(comment)
                .leftJoin(photo)
                .on(comment.user.id.eq(photo.boardId).and(photo.boardType.eq(USER)))
                .select(new QCommentResponseDto(
                        comment.id,
                        comment.title,
                        comment.content,
                        comment.user.id,
                        comment.user.userName,
                        photo.path,
                        comment.review.id,
                        comment.review.perfume.subject,
                        comment.createDateTime
                ))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<CommentResponseDto> page = PageUtils.makePageList(dtoList, pageable);

        return page;
    }


    // 특정 리뷰에 달린 댓글(페이지네이션)
    @Override
    public Page<CommentResponseDto> getReviewCommentList(Long reviewId, Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<CommentResponseDto> dtoList = from(comment)
                .leftJoin(photo)
                .on(comment.user.id.eq(photo.boardId).and(photo.boardType.eq(USER)))
                .where(comment.review.id.eq(reviewId))
                .select(new QCommentResponseDto(
                        comment.id,
                        comment.title,
                        comment.content,
                        comment.user.id,
                        comment.user.userName,
                        photo.path,
                        comment.review.id,
                        comment.review.perfume.subject,
                        comment.createDateTime
                ))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<CommentResponseDto> page = PageUtils.makePageList(dtoList, pageable);

        return page;
    }


    // 특정 리뷰에 달린 댓글(개수 정해서)
    @Override
    public List<CommentResponseDto> findByReviewId(Long reviewId, int size) {

        List<CommentResponseDto> dtoList = from(comment)
                .leftJoin(photo)
                .on(comment.user.id.eq(photo.boardId).and(photo.boardType.eq(USER)))
                .where(comment.review.id.eq(reviewId))
                .select(new QCommentResponseDto(
                        comment.id,
                        comment.title,
                        comment.content,
                        comment.user.id,
                        comment.user.userName,
                        photo.path,
                        comment.review.id,
                        comment.review.perfume.subject,
                        comment.createDateTime
                ))
                .orderBy(comment.createDateTime.desc())
                .limit(size)
                .fetch();

        return dtoList;
    }


    // Pageable에서 정렬기준 추출 : Comment는 시간 순 내림/오름차순만 가능
    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                OrderSpecifier<?> orderDateTime = QueryDslUtil.getSortedColumn(direction, comment.createDateTime, "createDateTime");
                ORDERS.add(orderDateTime);
            }
        }
        return ORDERS;
    }


}
