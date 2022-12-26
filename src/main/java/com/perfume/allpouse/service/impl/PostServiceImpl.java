package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.config.security.TokenProvider;
import com.perfume.allpouse.data.entity.*;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.PostResponseDto;
import com.perfume.allpouse.model.dto.QPostResponseDto;
import com.perfume.allpouse.model.dto.SavePostDto;
import com.perfume.allpouse.model.enums.BulletinType;
import com.perfume.allpouse.model.enums.Permission;
import com.perfume.allpouse.service.PhotoService;
import com.perfume.allpouse.service.PostService;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.model.enums.BoardType.POST;
import static com.perfume.allpouse.model.enums.BulletinType.*;
import static com.perfume.allpouse.model.enums.Permission.*;

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

    QPost post = new QPost("post");
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

        List<PostResponseDto> postList = queryFactory.select(new QPostResponseDto(post.id, post.type, post.title, post.content, photo.path, post.hitCnt, post.recommendCnt, post.user.id, post.user.userName, post.createDateTime))
                .from(post)
                .leftJoin(photo)
                .on(post.id.eq(photo.boardId).and(photo.boardType.eq(POST)))
                .where(post.createDateTime.between(now.minusMonths(3), now))
                .orderBy(post.recommendCnt.desc())
                .limit(size)
                .fetch();

        return postList;
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
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }
}
