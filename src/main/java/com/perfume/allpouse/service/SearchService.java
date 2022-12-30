package com.perfume.allpouse.service;

import com.perfume.allpouse.model.dto.SearchResultDto;

public interface SearchService {

    // 기본검색(with 키워드)
    SearchResultDto searchWithKeyword(String keyword);



}
