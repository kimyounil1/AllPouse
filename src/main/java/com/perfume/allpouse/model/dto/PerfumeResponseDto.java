package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PerfumeResponseDto {

    private Long id;

    private String subject;

    private Long brandId;

    private String brandName;

    private List<String> imagePath;

    @QueryProjection
    public PerfumeResponseDto(Long id, String subject, Long brandId, String brandName, List<String> imagePath) {
        this.id = id;
        this.subject = subject;
        this.brandId = brandId;
        this.brandName = brandName;
        this.imagePath = imagePath;
    }
}
