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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ReviewBoardRepository reviewRepository;


    // 댓글 저장
    @Override
    @Transactional
    public Long save(SaveCommentDto saveCommentDto) {

    }


    // 댓글 수정
    @Override
    @Transactional
    public Long update(SaveCommentDto saveCommentDto) {
        return null;
    }


    // 댓글 삭제
    @Override
    @Transactional
    public void delete(Long id) {

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
    public Comment toEntity(SaveCommentDto commentDto) {

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
