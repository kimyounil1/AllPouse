package com.perfume.allpouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResultDto {

    private List<SearchBrandDto> brands;

    private int brandCnt;

    private List<SearchPerfumeDto> perfumes;

    private int perfumeCnt;

    private List<SearchReviewDto> reviews;

    private int reviewCnt;

    private List<SearchPostDto> posts;

    private int postCnt;
}
