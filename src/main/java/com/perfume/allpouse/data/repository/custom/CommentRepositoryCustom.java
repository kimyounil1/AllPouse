package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {

    // 유저가 작성한 향수 리뷰 댓글 페이지네이션 후 전달
    Page<CommentResponseDto> getUserCommentDtoList(Long userId, Pageable pageable);

    // 전체 댓글에 대한 CommentResponseDto List 가져옴
    Page<CommentResponseDto> getAllCommentDtoList(Pageable pageable);

    // 리뷰에 대한 CommentResponseDto List 가져옴
    Page<CommentResponseDto> getReviewCommentList(Long reviewId, Pageable pageable);

    // 리뷰에 대한 CommentResposneDto List 가져옴(개수 정해서)
    List<CommentResponseDto> findByReviewId(Long reviewId, int size);
}
