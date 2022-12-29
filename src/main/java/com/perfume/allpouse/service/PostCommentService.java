package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.model.dto.PostCommentResponseDto;
import com.perfume.allpouse.model.dto.SavePostCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCommentService {

    // 저장, 업데이트, 삭제
    Long save(SavePostCommentDto postCommentDto);

    Long update(SavePostCommentDto postCommentDto);

    void delete(Long commentId);



    // 조회
    PostComment findOne(Long commentId);

    Page<PostCommentResponseDto> getUserPostCommentList(Long userId, Pageable pageable);

    List<PostCommentResponseDto> getPostCommentList(Long postId);

    boolean isRecommended(Long commentId, Long userId);


    // 기타 로직
    int updateRecommendCnt(Long commentId, Long userId);
}
