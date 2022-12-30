package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.SearchPostDto;

import java.util.List;

public interface PostRepositoryCustom {

    // 기본검색
    List<SearchPostDto> search(String keyword);
}
