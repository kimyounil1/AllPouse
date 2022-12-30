package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SearchPostDto {

    private Long id;

    private String type;

    private String title;

    private String content;

    private int hitCnt;

    private int recommendCnt;

    private Long userId;

    private String userName;


    @QueryProjection
    public SearchPostDto(Long id, String type, String title, String content, int hitCnt, int recommendCnt, Long userId, String userName) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.hitCnt = hitCnt;
        this.recommendCnt = recommendCnt;
        this.userId = userId;
        this.userName = userName;
    }
}