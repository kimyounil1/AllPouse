package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
public class PerfumeInfoDto {

    private Long id;

    private String subject;

    private String brandName;

    private int price;

    private String content;

    private int hitCnt;

    private List<String> path;

    private double score;


    @QueryProjection
    public PerfumeInfoDto(Long id, String subject, String brandName, int price, String content, int hitCnt, List<String> path, double score) {
        this.id = id;
        this.subject = subject;
        this.brandName = brandName;
        this.price = price;
        this.content = content;
        this.hitCnt = hitCnt;
        this.path = path;
        this.score = score;
    }
}
