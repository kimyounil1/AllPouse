package com.perfume.allpouse.data.repository.custom;

import com.perfume.allpouse.model.dto.SearchBrandDto;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BrandRepositoryCustom {

    // 기본검색
    List<SearchBrandDto> search(String keyword);
}
