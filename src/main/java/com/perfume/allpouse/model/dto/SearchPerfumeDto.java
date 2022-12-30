package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class SearchPerfumeDto {

    private Long id;

    private String subject;

    private Long brandId;

    private String brandName;

    private List<String> path;


    @QueryProjection
    public SearchPerfumeDto(Long id, String subject, Long brandId, String brandName, List<String> path) {
        this.id = id;
        this.subject = subject;
        this.brandId = brandId;
        this.brandName = brandName;
        this.path = path;
    }
}
