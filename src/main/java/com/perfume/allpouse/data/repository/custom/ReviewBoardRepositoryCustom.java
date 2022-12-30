package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.SearchReviewDto;

import java.util.List;

public interface ReviewBoardRepositoryCustom {

    // 기본검색
    List<SearchReviewDto> search(String keyword);
}
