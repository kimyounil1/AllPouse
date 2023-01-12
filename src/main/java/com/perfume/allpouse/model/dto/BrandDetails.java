package com.perfume.allpouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BrandDetails<T> {

    private T infoDto;

    private List<PerfumeResponseDto> perfumes;

    private List<ReviewResponseDto> perfumerReviews;

    private List<ReviewResponseDto> userReviews;

    private List<ReviewResponseDto> highRecommendReviews;

}
