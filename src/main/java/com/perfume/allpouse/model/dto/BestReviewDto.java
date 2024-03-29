package com.perfume.allpouse.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BestReviewDto<T> {

    private T infoDto;

    private List<ReviewResponseDto> perfumerReviews;

    private List<ReviewResponseDto> userReviews;

    private List<ReviewResponseDto> highRecommendReviews;
}
