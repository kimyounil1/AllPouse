package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.ReviewBoard;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveReviewDto {

    private Long id;

    private String subject;

    private String content;

    private Long userId;

    private Long perfumeId;



    // ReviewBoard -> Dto
    public SaveReviewDto(ReviewBoard reviewBoard) {
        this.id = reviewBoard.getId();
        this.subject = reviewBoard.getSubject();
        this.userId = reviewBoard.getUser().getId();
        this.perfumeId = reviewBoard.getPerfume().getId();
    }


    public SaveReviewDto(String subject, String content, Long userId, Long perfumeId) {
        this.subject = subject;
        this.content = content;
        this.userId = userId;
        this.perfumeId = perfumeId;
    }
}
