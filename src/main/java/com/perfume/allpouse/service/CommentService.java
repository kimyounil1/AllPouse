package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.Comment;
import com.perfume.allpouse.model.dto.SaveCommentDto;

import java.util.List;

public interface CommentService {

    Long save(SaveCommentDto saveCommentDto);

    Long update(SaveCommentDto saveCommentDto);
    
    void delete(Long id);

    List<Comment> findByUserId(Long id);

    List<Comment> findByReviewId(Long id);

    Comment findOne(Long id);





}
