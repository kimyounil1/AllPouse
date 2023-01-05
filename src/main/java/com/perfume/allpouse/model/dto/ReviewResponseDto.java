package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.ReviewBoard;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ReviewResponseDto {

    private Long id;

    private String userName;

    private String subject;

    private String content;

    private String perfumeName;

    private String brandName;

    private int hitCnt;

    private int recommendCnt;

    private List<String> images;

    private LocalDateTime createDateTime;


    @QueryProjection
    public ReviewResponseDto(Long id, String userName, String subject, String content, String perfumeName, String brandName, int hitCnt, int recommendCnt, List<String> images, LocalDateTime createDateTime) {
        this.id = id;
        this.userName = userName;
        this.subject = subject;
        this.content = content;
        this.perfumeName = perfumeName;
        this.brandName = brandName;
        this.hitCnt = hitCnt;
        this.recommendCnt = recommendCnt;
        this.createDateTime = createDateTime;
        this.images = images;
    }
}

