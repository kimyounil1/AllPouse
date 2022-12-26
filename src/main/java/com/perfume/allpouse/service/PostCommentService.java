package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.model.dto.SavePostCommentDto;

public interface PostCommentService {

    Long save(SavePostCommentDto postCommentDto);

    Long update(SavePostCommentDto postCommentDto);

    void delete(Long id);

    PostComment findOne(Long id);
}
