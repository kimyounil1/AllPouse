package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.PerfumeResponseDto;
import com.perfume.allpouse.model.dto.SearchPerfumeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PerfumeBoardRepositoryCustom {

    // 기본검색
    List<SearchPerfumeDto> search(String keyword);

    // 조회수 순 향수 페이징
    Page<PerfumeResponseDto> getPerfumeByHitCnt(Pageable pageable);

    // 특정 브랜드의 향수 가져옴(조회수 순)
    List<PerfumeResponseDto> getPerfumeByBrandId(Long brandId, int size);

    //

}
