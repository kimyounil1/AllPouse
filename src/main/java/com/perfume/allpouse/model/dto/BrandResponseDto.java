package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BrandResponseDto {

    private Long id;

    private String name;

    private List<String> imagePath;

    private int perfumeCnt;


    @QueryProjection
    public BrandResponseDto(Long id, String name, List<String> imagePath, int perfumeCnt) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.perfumeCnt = perfumeCnt;
    }
}
