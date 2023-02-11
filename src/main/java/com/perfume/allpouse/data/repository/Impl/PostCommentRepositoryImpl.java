package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.entity.QPostComment;
import com.perfume.allpouse.data.repository.custom.PostCommentRepositoryCustom;
import com.perfume.allpouse.model.dto.PostCommentResponseDto;
import com.perfume.allpouse.model.dto.QPostCommentResponseDto;
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

public class PostCommentRepositoryImpl extends QuerydslRepositorySupport implements PostCommentRepositoryCustom {

    public PostCommentRepositoryImpl() {
        super(PostComment.class);
    }

    QPostComment postComment = QPostComment.postComment;
    QPhoto userPhoto = new QPhoto("userPhoto");


    // 게시글에 달린 댓글
    @Override
    public List<PostCommentResponseDto> getPostCommentList(Long postId) {

        List<PostCommentResponseDto> commentDtoList = from(postComment)
                .leftJoin(userPhoto)
                .on(postComment.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(postComment.post.id.eq(postId))
                .select(new QPostCommentResponseDto(
                        postComment.id,
                        postComment.content,
                        postComment.recommendCnt,
                        postComment.post.id,
                        postComment.user.id,
                        postComment.user.userName,
                        userPhoto.path,
                        postComment.referCommentId,
                        postComment.createDateTime
                ))
                .orderBy(postComment.createDateTime.desc())
                .fetch();

        return commentDtoList;
    }

    @Override
    public Page<PostCommentResponseDto> getPostCommentPageList(Long postId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<PostCommentResponseDto> commentDtoList = from(postComment)
                .leftJoin(userPhoto)
                .on(postComment.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(postComment.post.id.eq(postId))
                .select(new QPostCommentResponseDto(
                        postComment.id,
                        postComment.content,
                        postComment.recommendCnt,
                        postComment.post.id,
                        postComment.user.id,
                        postComment.user.userName,
                        userPhoto.path,
                        postComment.referCommentId,
                        postComment.createDateTime
                ))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<PostCommentResponseDto> pages = PageUtils.makePageList(commentDtoList, pageable);

        return pages;
    }

    // 게시글에 달린 댓글(+페이지네이션)



    // 유저가 작성한 게시글(+페이지네이션)
    @Override
    public Page<PostCommentResponseDto> getUserPostCommentList(Long userId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<PostCommentResponseDto> commentDtoList = from(postComment)
                .leftJoin(userPhoto)
                .on(userPhoto.boardId.eq(userId).and(userPhoto.boardType.eq(USER)))
                .where(postComment.user.id.eq(userId))
                .select(new QPostCommentResponseDto(
                        postComment.id,
                        postComment.content,
                        postComment.recommendCnt,
                        postComment.post.id,
                        postComment.user.id,
                        postComment.user.userName,
                        userPhoto.path,
                        postComment.referCommentId,
                        postComment.createDateTime
                ))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<PostCommentResponseDto> commentPages = PageUtils.makePageList(commentDtoList, pageable);

        return commentPages;
    }


    // Pageable에서 정렬기준 추출 : PostComment는 시간 순 내림/오름차순만 가능
    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                OrderSpecifier<?> orderDateTime = QueryDslUtil.getSortedColumn(direction, QPostComment.postComment.createDateTime, "createDateTime");
                ORDERS.add(orderDateTime);
            }
        }
        return ORDERS;
    }
}
