package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewResponseDto {

    private Long id;

    private Long userId;

    private String userName;

    private List<String> userImage;

    private String subject;

    private String content;

    private String perfumeName;

    private String brandName;

    private int hitCnt;

    private int recommendCnt;

    private List<String> images;

    private int commentCnt;

    private int score;

    private boolean isRecommended;

    private LocalDateTime createDateTime;


    @QueryProjection
    public ReviewResponseDto(Long id, Long userId, List<String> userImage, String userName, String subject, String content, String perfumeName, String brandName, int hitCnt, int recommendCnt, List<String> images, int commentCnt, int score, LocalDateTime createDateTime) {
        this.id = id;
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.subject = subject;
        this.content = content;
        this.perfumeName = perfumeName;
        this.brandName = brandName;
        this.hitCnt = hitCnt;
        this.recommendCnt = recommendCnt;
        this.images = images;
        this.commentCnt = commentCnt;
        this.score = score;
        this.createDateTime = createDateTime;
    }

}

