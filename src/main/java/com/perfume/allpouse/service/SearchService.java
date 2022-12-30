package com.perfume.allpouse.service;

import com.perfume.allpouse.model.dto.PostResponseDto;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SearchResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    // 기본검색(with 키워드)
    SearchResultDto searchWithKeyword(String keyword);

    // 게시글 기본검색(with 키워드) + 페이지네이션
    Page<PostResponseDto> searchPostWithKeyword(String keyword, Pageable pageable);

    // 리뷰 기본검색(with 키워드) + 페이지네이션
    Page<ReviewResponseDto> searchReviewWithKeyword(String keyword, Pageable pageable);

}
