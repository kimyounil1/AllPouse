package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.PostCommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCommentRepositoryCustom {

    /**
     * PostComment -> 게시글에 달린 댓글
     */

    // 게시글에 달린 댓글
    List<PostCommentResponseDto> getPostCommentList(Long postId);

    // 게시글에 달린 댓글 + 페이지네이션
    Page<PostCommentResponseDto> getPostCommentPageList(Long postId, Pageable pageable);

    // 유저가 작성한 댓글
    Page<PostCommentResponseDto> getUserPostCommentList(Long userId, Pageable pageable);


}
