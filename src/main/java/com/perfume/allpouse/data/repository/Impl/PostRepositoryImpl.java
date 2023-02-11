package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.entity.QPost;
import com.perfume.allpouse.data.repository.custom.PostRepositoryCustom;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.BannerResponseDto;
import com.perfume.allpouse.model.dto.PostResponseDto;
import com.perfume.allpouse.model.dto.QBannerResponseDto;
import com.perfume.allpouse.model.dto.QPostResponseDto;
import com.perfume.allpouse.model.enums.BulletinType;
import com.perfume.allpouse.service.PhotoService;
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

import static com.perfume.allpouse.exception.ExceptionEnum.BOARD_TYPE_ID_NOT_FOUND;
import static com.perfume.allpouse.model.enums.BoardType.POST;
import static com.perfume.allpouse.model.enums.BoardType.USER;
import static com.perfume.allpouse.model.enums.BulletinType.*;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    private final PhotoService photoService;


    QPost post = QPost.post;
    QPhoto photo = new QPhoto("photo");
    QPhoto userPhoto = new QPhoto("userPhoto");


    public PostRepositoryImpl(PhotoService photoService) {
        super(Post.class);
        this.photoService = photoService;
    }

    // 기본검색
    @Override
    public List<PostResponseDto> search(String keyword) {

        List<PostResponseDto> posts = from(this.post)
                .leftJoin(photo)
                .on((this.post.id.eq(photo.boardId).and(photo.boardType.eq(POST))))
                .leftJoin(userPhoto)
                .on(post.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(this.post.title.containsIgnoreCase(keyword).or(this.post.content.containsIgnoreCase(keyword)))
                .select(new QPostResponseDto(
                        this.post.id,
                        this.post.type,
                        this.post.title,
                        this.post.content,
                        this.photo.path,
                        this.post.hitCnt,
                        this.post.recommendCnt,
                        this.post.user.id,
                        this.post.user.userName,
                        this.userPhoto.path,
                        this.post.postComments.size(),
                        this.post.createDateTime))
                .orderBy(this.post.title.asc())
                .limit(10)
                .fetch();

        return posts;
    }


    // 기본검색 -> 전체 게시글
    @Override
    public Page<PostResponseDto> searchWithPaging(String keyword, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<PostResponseDto> posts = from(this.post)
                .leftJoin(photo)
                .on((this.post.id.eq(photo.boardId).and(photo.boardType.eq(POST))))
                .leftJoin(userPhoto)
                .on(post.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(this.post.title.containsIgnoreCase(keyword).or(this.post.content.containsIgnoreCase(keyword)))
                .select(new QPostResponseDto(
                        this.post.id,
                        this.post.type,
                        this.post.title,
                        this.post.content,
                        this.photo.path,
                        this.post.hitCnt,
                        this.post.recommendCnt,
                        this.post.user.id,
                        this.post.user.userName,
                        this.userPhoto.path,
                        this.post.postComments.size(),
                        this.post.createDateTime))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<PostResponseDto> pages = PageUtils.makePageList(posts, pageable);

        return pages;
    }

    @Override
    public Page<PostResponseDto> getPostByBoardType(Long boardId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        BulletinType boardType = toBulletinType(boardId);

        List<PostResponseDto> posts = from(post)
                .leftJoin(photo)
                .on(post.id.eq(photo.boardId).and(photo.boardType.eq(POST)))
                .leftJoin(userPhoto)
                .on(post.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(post.type.eq(boardType))
                .select(new QPostResponseDto(
                        post.id,
                        post.type,
                        post.title,
                        post.content,
                        photo.path,
                        post.hitCnt,
                        post.recommendCnt,
                        post.user.id,
                        post.user.userName,
                        userPhoto.path,
                        this.post.postComments.size(),
                        post.createDateTime
                ))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        Page<PostResponseDto> pages = PageUtils.makePageList(posts, pageable);

        return pages;
    }


    // 배너게시글 가져오기
    @Override
    public List<BannerResponseDto> getBannerPost() {

        List<BannerResponseDto> bannerPosts = from(post)
                .leftJoin(photo)
                .on(post.id.eq(photo.boardId).and(photo.boardType.eq(POST)))
                .where(post.type.eq(BANNER))
                .select(new QBannerResponseDto(post.id, photo.path, post.createDateTime))
                .orderBy(post.createDateTime.desc())
                .fetch();

        return bannerPosts;
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
                        OrderSpecifier<?> orderName = QueryDslUtil.getSortedColumn(direction, QPost.post.recommendCnt, "recommendCnt");
                        ORDERS.add(orderName);
                        break;

                    // 작성일 순
                    case "createDateTime":
                        OrderSpecifier<?> orderDateTime = QueryDslUtil.getSortedColumn(direction, QPost.post.createDateTime, "createDateTime");
                        ORDERS.add(orderDateTime);
                        break;

                    default:
                        break;
                }
            }
        }
        return ORDERS;
    }


    // boardId로 BulletinType 가져오는 메소드
    private BulletinType toBulletinType(Long boardId) {
        // 자유게시판
        if (boardId == 1L){
            return FREE;
        } else if (boardId == 2L) {
            return PERFUMER;
        } else {
            throw new CustomException(BOARD_TYPE_ID_NOT_FOUND);
        }
    }
}
