package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class SearchReviewDto {

    private Long id;

    private String subject;

    private String content;

    private Long userId;

    private String userName;

    private int recommendCnt;

    private int commentCnt;

    private List<String> path;


    @QueryProjection
    public SearchReviewDto(Long id, String subject, String content, Long userId, String userName, int recommendCnt, int commentCnt, List<String> path) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.recommendCnt = recommendCnt;
        this.commentCnt = commentCnt;
        this.path = path;
    }
}
