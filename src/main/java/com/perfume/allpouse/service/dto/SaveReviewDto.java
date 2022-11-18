package com.perfume.allpouse.service.dto;

import com.perfume.allpouse.data.entity.ReviewBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SaveReviewDto {

    private Long id;

    private String subject;

    private String content;

    private Long userId;

    private Long perfumeId;

    private String imagePath;


    public SaveReviewDto(ReviewBoard reviewBoard) {
        this.id = reviewBoard.getId();
        this.subject = reviewBoard.getSubject();
        this.userId = reviewBoard.getUser().getId();
        this.perfumeId = reviewBoard.getPerfume().getId();
        this.imagePath = reviewBoard.getImagePath();
    }

    public SaveReviewDto(String subject, String content, Long userId, Long perfumeId, String imagePath) {
        this.subject = subject;
        this.content = content;
        this.userId = userId;
        this.perfumeId = perfumeId;
        this.imagePath = imagePath;
    }
}