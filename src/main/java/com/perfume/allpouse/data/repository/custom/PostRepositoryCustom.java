package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    // 기본검색
    List<PostResponseDto> search(String keyword);

    // 기본검색 -> 전체 게시글
    Page<PostResponseDto> searchWithPaging(String keyword, Pageable pageable);
}
