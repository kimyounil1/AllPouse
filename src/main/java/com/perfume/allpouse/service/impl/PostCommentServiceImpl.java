package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.data.entity.QPostComment;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.PostCommentRepository;
import com.perfume.allpouse.data.repository.PostRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.PostCommentResponseDto;
import com.perfume.allpouse.model.dto.QPostCommentResponseDto;
import com.perfume.allpouse.model.dto.SavePostCommentDto;
import com.perfume.allpouse.service.PostCommentService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;
import static com.perfume.allpouse.exception.ExceptionEnum.POST_COMMENT_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentServiceImpl implements PostCommentService {

    private final Logger LOGGER = LoggerFactory.getLogger(PostCommentService.class);

    private final PostCommentRepository postCommentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final JPAQueryFactory queryFactory;

    QPostComment postComment = new QPostComment("postComment");


    // 댓글 저장
    @Override
    @Transactional
    public Long save(SavePostCommentDto postCommentDto) {

        Long commentId = postCommentDto.getId();

        // 등록된 적 없는 댓글 -> save
        if (commentId == null) {
            PostComment comment = postCommentRepository.save(toEntity(postCommentDto));
            return comment.getId();
        }

        // 등록된 적 있는 댓글 -> update
        else {
            update(postCommentDto);
            return commentId;
        }
    }


    // 댓글 업데이트
    @Override
    @Transactional
    public Long update(SavePostCommentDto postCommentDto) {

        Long commentId = postCommentDto.getId();
        Optional<PostComment> comment = postCommentRepository.findById(commentId);

        if (comment.isPresent()) {
            comment.get().changeComment(postCommentDto);
            return commentId;
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }


    // 댓글 삭제
    @Override
    @Transactional
    public void delete(Long id) {
        Optional<PostComment> comment = postCommentRepository.findById(id);

        if (comment.isPresent()) {
            postCommentRepository.delete(comment.get());
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }


    // 게시글 댓글 단건조회
    @Override
    public PostComment findOne(Long id) {
        Optional<PostComment> comment = postCommentRepository.findById(id);

        if (comment.isPresent()) {
            return comment.get();
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }


    // 유저가 작성한 게시글 댓글 페이지네이션 후 전달
    @Override
    public Page<PostCommentResponseDto> getUserPostCommentList(Long userId, Pageable pageable) {

        Page<PostComment> comments = postCommentRepository.findPostCommentsByUserId(userId, pageable);

        Page<PostCommentResponseDto> dtoPage = comments.map(new Function<PostComment, PostCommentResponseDto>() {

            @Override
            public PostCommentResponseDto apply(PostComment postComment) {
                return PostCommentResponseDto.toDto(postComment);
            }
        });

        return dtoPage;
    }


    // 게시글에 달린 댓글
    // 정렬 : createDateTime / DESC
    @Override
    public List<PostCommentResponseDto> getPostCommentList(Long postId) {

        return queryFactory
                .select(new QPostCommentResponseDto(postComment.id, postComment.content, postComment.recommendCnt,
                        postComment.post.id, postComment.user.id, postComment.user.userName, postComment.referCommentId, postComment.createDateTime))
                .from(postComment)
                .where(postComment.post.id.eq(postId))
                .orderBy(postComment.createDateTime.desc())
                .fetch();
    }


    // 유저가 (게시글) 댓글 추천했는지 여부
    @Override
    public boolean isRecommended(Long commentId, Long userId) {

        PostComment comment = postCommentRepository.findById(commentId).get();
        List<Long> userList = comment.getRecommendUserList();

        return userList.contains(userId);
    }


    // (게시글) 댓글 추천 기능
    // 처음 추천 : 0 / 이미 추천한 댓글 : 1
    @Override
    @Transactional
    public int updateRecommendCnt(Long commentId, Long userId) {

        Optional<PostComment> commentOpt = postCommentRepository.findById(commentId);

        if (commentOpt.isEmpty()) {
            throw new CustomException(POST_COMMENT_ID_NOT_FOUND);
        } else {
            PostComment comment = commentOpt.get();
            List<Long> userList = comment.getRecommendUserList();

            // 해당 댓글 추천한 사람 없거나, 유저가 게시글 추천한 적 X -> 0 (추천)
            if (userList == null || !userList.contains(userId)) {
                userList.add(userId);
                postCommentRepository.addRecommendCnt(commentId);
                return 0;
            }
            // 추천한 적 O -> 1 (추천취소)
            else {
                userList.remove(userId);
                postCommentRepository.minusRecommendCnt(commentId);
                return 1;
            }
        }
    }


    // Dto -> PostComment
    private PostComment toEntity(SavePostCommentDto commentDto) {

        Post post = postRepository.findById(commentDto.getPostId()).get();
        User user = userRepository.findById(commentDto.getUserId()).get();


        PostComment comment = PostComment.builder()
                .content(commentDto.getContent())
                .referCommentId(commentDto.getReferCommentId())
                .build();

        setPostAndUser(comment, post, user);

        return comment;
    }


    private void setPostAndUser(PostComment postComment, Post post, User user) {
        postComment.setPost(post);
        postComment.setUser(user);
    }
}