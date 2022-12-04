package com.perfume.allpouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PerfumePageDto {

    private PerfumeInfoDto perfumeInfoDto;

    private BestReviewDto reviewDto;
}
