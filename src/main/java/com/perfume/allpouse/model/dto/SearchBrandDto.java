package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class SearchBrandDto {

    private Long id;

    private String name;

    private List<String> path;


    @QueryProjection
    public SearchBrandDto(Long id, String name, List<String> path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }
}
