package com.perfume.allpouse.service.impl;


import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.CommentRepository;
import com.perfume.allpouse.data.repository.ReviewBoardRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.model.dto.SaveCommentDto;
import com.perfume.allpouse.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ReviewBoardRepository reviewRepository;


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
            throw new IllegalStateException("존재하지 않는 댓글입니다.");
        }
    }


    /*
     * 댓글 조회 메소드
     */
    //
    @Override
    public List<Comment> findByUserId(Long id) {
        return null;
    }

    //
    @Override
    public List<Comment> findByReviewId(Long id) {
        return null;
    }

    //
    @Override
    public Comment findById(Long id) {
        return null;
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
