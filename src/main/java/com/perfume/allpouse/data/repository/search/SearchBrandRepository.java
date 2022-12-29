package com.perfume.allpouse.data.repository.search;

import com.perfume.allpouse.model.dto.SearchBrandDto;

import java.util.List;


public interface SearchBrandRepository {

    // 기본검색
    List<SearchBrandDto> search(String keyword);
}
