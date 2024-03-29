package com.perfume.allpouse.service.impl;


import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.entity.QComment;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.CommentRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.CommentResponseDto;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.service.CommentService;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ReviewBoardRepository reviewRepository;

    private final JPAQueryFactory queryFactory;

    QComment comment = new QComment("comment");


    // 댓글 저장
    @Override
    @Transactional
    public Long save(SaveCommentDto saveCommentDto) {

        Comment comment = toEntity(saveCommentDto);
        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }


    // 댓글 수정
    @Override
    @Transactional
    public Long update(SaveCommentDto saveCommentDto) {

        Comment comment = commentRepository.findById(saveCommentDto.getId()).get();
        comment.changeComment(saveCommentDto);

        return comment.getId();
    }


    // 댓글 삭제
    @Override
    @Transactional
    public void delete(Long id) {

        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }


    /*
     * 댓글 조회 메소드
     */
    // 회원이 작성한 전체 댓글 조회(파라미터 : user_id)
    // 기본정렬 : 작성일자(cre_dt)
    @Override
    public List<Comment> findByUserId(Long id) {

        List<Comment> comments = commentRepository.findCommentsByUserId(id);

        if (comments.size() == 0) {
            throw new CustomException(INVALID_PARAMETER);
        } else {
            return comments;
        }
    }


    // 리뷰에 대한 전체 댓글 조회(파라미터: 리뷰id, 데이터 개수)
    // 기본정렬 : 작성일자(cre_dt)
    @Override
    public List<CommentResponseDto> findByReviewId(Long reviewId, int size) {

        List<CommentResponseDto> commentDtoList = commentRepository.findByReviewId(reviewId, size);

        return commentDtoList;
    }


    // 댓글 단건 조회(파라미터 : comment_id)
    @Override
    public Comment findOne(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()) {
            throw new CustomException(INVALID_PARAMETER);
        } else {
            return comment.get();
        }
    }


    // 유저가 작성한 향수리뷰 댓글 페이지네이션 후 전달
    @Override
    public Page<CommentResponseDto> getUserCommentList(Long userId, Pageable pageable) {

        Page<CommentResponseDto> pages = commentRepository.getUserCommentDtoList(userId, pageable);

        return pages;
    }


    // 전체 댓글 DTO 가져옴
    @Override
    public Page<CommentResponseDto> getAllCommentsList(Pageable pageable) {

        Page<CommentResponseDto> pages = commentRepository.getAllCommentDtoList(pageable);

        return pages;
    }


    // 리뷰에 대한 댓글 DTO 가져옴
    @Override
    public Page<CommentResponseDto> getReviewCommentList(Long reviewId, Pageable pageable) {

        Page<CommentResponseDto> pages = commentRepository.getReviewCommentList(reviewId, pageable);

        return pages;
    }



    // Dto -> Comment
    private Comment toEntity(SaveCommentDto commentDto) {

        Long userId = commentDto.getUserId();
        Long reviewId = commentDto.getReviewId();

        User user = userRepository.findById(userId).get();
        ReviewBoard review = reviewRepository.findById(reviewId).get();

        Comment comment = Comment.builder()
                .id(commentDto.getId())
                .title(commentDto.getTitle())
                .content(commentDto.getContent())
                .build();

        addUserAndReview(comment, user, review);

        return comment;
    }


    private void addUserAndReview(Comment comment, User user, ReviewBoard review) {
        comment.setUser(user);
        comment.setReview(review);
    }


}
