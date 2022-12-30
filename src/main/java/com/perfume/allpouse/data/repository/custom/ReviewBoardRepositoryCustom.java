package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.SearchReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewBoardRepositoryCustom {

    // 기본검색
    List<ReviewResponseDto> search(String keyword);

    // 기본검색 -> 전체 리뷰
    Page<ReviewResponseDto> searchWithPaging(String keyword, Pageable pageable);


}
