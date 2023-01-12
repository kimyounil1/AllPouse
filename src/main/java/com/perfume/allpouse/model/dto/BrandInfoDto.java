package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BrandInfoDto {

    private Long id;

    private String name;

    private String content;

    private List<String> path;

    private int hitCnt;


    @QueryProjection
    public BrandInfoDto(Long id, String name, String content, List<String> path, int hitCnt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.path = path;
        this.hitCnt = hitCnt;
    }
}
