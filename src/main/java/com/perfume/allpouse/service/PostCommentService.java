package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.model.dto.PostCommentResponseDto;
import com.perfume.allpouse.model.dto.SavePostCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCommentService {

    Long save(SavePostCommentDto postCommentDto);

    Long update(SavePostCommentDto postCommentDto);

    void delete(Long commentId);

    PostComment findOne(Long commentId);

    Page<PostCommentResponseDto> getUserPostCommentList(Long userId, Pageable pageable);

    List<PostCommentResponseDto> getPostCommentList(Long postId);
}
