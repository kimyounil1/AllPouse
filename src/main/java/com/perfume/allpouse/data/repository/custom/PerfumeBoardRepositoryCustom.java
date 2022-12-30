package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.SearchPerfumeDto;

import java.util.List;

public interface PerfumeBoardRepositoryCustom {

    // 기본검색
    List<SearchPerfumeDto> search(String keyword);
}
