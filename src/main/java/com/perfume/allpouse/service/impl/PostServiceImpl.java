package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.*;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.BannerResponseDto;
import com.perfume.allpouse.model.dto.PostResponseDto;
import com.perfume.allpouse.model.dto.QPostResponseDto;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.PostService;
import com.perfume.allpouse.utils.PageUtils;
import com.perfume.allpouse.utils.QueryDslUtil;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.exception.ExceptionEnum.POST_ID_NOT_FOUND;
import static com.perfume.allpouse.model.enums.BoardType.POST;
import static com.perfume.allpouse.model.enums.BoardType.USER;
import static com.perfume.allpouse.model.enums.BulletinType.*;
import static com.perfume.allpouse.model.enums.Permission.ROLE_ADMIN;
import static com.perfume.allpouse.model.enums.Permission.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PhotoService photoService;

    private final UserRepository userRepository;

    private final JPAQueryFactory queryFactory;

    private final EntityManager em;

    /**
     * 동적 쿼리 -> 전부 RepositoryCustom / RepositoryImpl로 옮기기
     */
    QPost post = new QPost("post");

    QPhoto userPhoto = new QPhoto("userPhoto");
    QPhoto photo = new QPhoto("photo");


    // 게시글 저장(사진X)
    @Override
    @Transactional
    public Long save(SavePostDto savePostDto, Permission role) {

        Long postId = savePostDto.getId();

        // 등록된 적 없는 글 -> save
        if (postId == null) {
            return checkBulletinType(savePostDto, role);
        }

        // 등록된 적 있는 글 -> 사진 삭제, 게시글 내용 변경
        else {
            photoService.delete(POST, postId);
            update(savePostDto);
            return postId;
        }
    }


    // 게시글 저장(사진O)
    @Override
    @Transactional
    public Long save(SavePostDto savePostDto, List<MultipartFile> photos, Permission role) throws IOException {

        Long postId = savePostDto.getId();

        // 등록된 적 없는 글 -> 글/사진 저장
        if (postId == null) {
            Long savedId = save(savePostDto, role);
            photoService.save(photos, POST, savedId);
            return savedId;
        }

        // 등록된 적 있는 글 -> 사진 삭제, 게시글 내용 변경
        else {
            photoService.delete(POST, postId);
            photoService.save(photos, POST, postId);
            update(savePostDto);
            return postId;
        }
    }


    // 게시글 업데이트
    @Override
    @Transactional
    public Long update(SavePostDto savePostDto) {

        Long postId = savePostDto.getId();
        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
            Post findPost = post.get();
            findPost.changePost(savePostDto);
            return postId;
        } else {throw new CustomException(INVALID_PARAMETER);}
    }


    // 게시글 삭제
    @Override
    @Transactional
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }


    // postId로 게시글 검색
    @Override
    public Post findOne(Long postId) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            throw new CustomException(INVALID_PARAMETER);
        } else {
            return post.get();
        }
    }


    // 인기 게시글 size개 가져옴
    // 기준이 되는 기간 : 3개월
    @Override
    public List<PostResponseDto> getPopularPost(int size) {

        LocalDateTime now = LocalDateTime.now();

        List<PostResponseDto> postList =
                queryFactory.select(new QPostResponseDto(post.id, post.type, post.title, post.content,
                                photo.path, post.hitCnt, post.recommendCnt, post.user.id, post.user.userName,
                                userPhoto.path,
                                post.createDateTime))
                .from(post)
                        .leftJoin(photo)
                        .on(post.id.eq(photo.boardId).and(photo.boardType.eq(POST)))
                        .leftJoin(userPhoto)
                        .on(post.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(post.createDateTime.between(now.minusMonths(3), now))
                .orderBy(post.recommendCnt.desc())
                .limit(size)
                .fetch();




        return postList;
    }


    // 게시글의 상세 내용 조회
    @Override
    @Transactional
    public PostResponseDto getPost(Long postId) {

        // 조회수 + 1
        postRepository.updateHitCnt(postId);

        return queryFactory
                .select(new QPostResponseDto(post.id, post.type, post.title, post.content, photo.path,
                        post.hitCnt, post.recommendCnt, post.user.id, post.user.userName,
                        userPhoto.path,
                        post.createDateTime))
                .from(post)
                .leftJoin(photo)
                .on(post.id.eq(photo.boardId).and(photo.boardType.eq(POST)))
                .leftJoin(userPhoto)
                .on(post.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(post.id.eq(postId))
                .fetchOne();
    }


    // 회원이 작성한 게시글을 ResponseDto 형식으로 페이지네이션해서 가져옴
    // 기본정렬 : 작성일자 기준 내림차순(pageable로 변경 가능)
    @Override
    public Page<PostResponseDto> getUserPostList(Long userId, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<PostResponseDto> postDtoList = queryFactory
                .select(new QPostResponseDto(post.id, post.type, post.title,
                        post.content, photo.path, post.hitCnt, post.recommendCnt,
                        post.user.id, post.user.userName,
                        userPhoto.path,
                        post.createDateTime))
                .from(post)
                .leftJoin(photo)
                .on(post.id.eq(photo.boardId).and(photo.boardType.eq(POST)))
                .leftJoin(userPhoto)
                .on(post.user.id.eq(userPhoto.boardId).and(userPhoto.boardType.eq(USER)))
                .where(post.user.id.eq(userId))
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        postDtoList.forEach(dto -> dto.setRecommended(isRecommended(dto.getId(), userId)));

        Page<PostResponseDto> pageList = PageUtils.makePageList(postDtoList, pageable);

        return pageList;
    }


    // 유저가 게시글 추천했는지 여부
    @Override
    public boolean isRecommended(Long postId, Long userId) {

        Post post = postRepository.findById(postId).get();
        List<Long> userList = post.getRecommendUserList();

        return userList.contains(userId);
    }


    // 배너 게시글 가져옴
    @Override
    public List<BannerResponseDto> getBannerPost() {

        return postRepository.getBannerPost();
    }


    // 게시글 추천 기능
    // 처음 추천 : 0 / 이미 추천한 게시물 : 1
    @Override
    @Transactional
    public int updateRecommendCnt(Long postId, Long userId) {

        Optional<Post> postOpt = postRepository.findById(postId);

        if (postOpt.isEmpty()) {
            throw new CustomException(POST_ID_NOT_FOUND);
        } else {
            Post post = postOpt.get();
            List<Long> userList = post.getRecommendUserList();

            // 해당 게시물 추천한 사람 없거나, 유저가 게시글 추천하지 X -> 0 (추천)
            if (userList == null || !userList.contains(userId)) {
                userList.add(userId);
                postRepository.addRecommendCnt(postId);
                return 0;
            }
            // 추천한 적 O -> 1 (추천취소)
            else {
                userList.remove(userId);
                postRepository.minusRecommendCnt(postId);
                return 1;
            }
        }
    }


    // 게시판타입 체크
    private Long checkBulletinType(SavePostDto savePostDto, Permission role) {
        User user = userRepository.findById(savePostDto.getUserId()).get();
        String type = savePostDto.getType();

        // 자유게시판 게시글
        if (type.equals("자유게시판")) {
            Post post = Post.builder()
                    .type(FREE)
                    .title(savePostDto.getTitle())
                    .content(savePostDto.getContent())
                    .user(user)
                    .build();

            return postRepository.save(post).getId();
        }

        // 조향사게시판 게시글
        else if (type.equals("조향사게시판") && role != ROLE_USER) {
            Post post = Post.builder()
                    .type(PERFUMER)
                    .title(savePostDto.getTitle())
                    .content(savePostDto.getContent())
                    .user(user)
                    .build();

            return postRepository.save(post).getId();
        }

        // 배너게시판 게시글
        else if (type.equals("배너게시판") && role == ROLE_ADMIN) {
            Post post = Post.builder()
                    .type(BANNER)
                    .title(savePostDto.getTitle())
                    .content(savePostDto.getContent())
                    .user(user)
                    .build();

            return postRepository.save(post).getId();
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
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
}
