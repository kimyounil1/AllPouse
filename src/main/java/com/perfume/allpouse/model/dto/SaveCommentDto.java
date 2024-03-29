package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Comment;
import lombok.*;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@ToString
public class SaveCommentDto {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private Long reviewId;


    // Comment -> Dto
    public SaveCommentDto(Comment comment) {
        this.id = comment.getId();
        this.title = comment.getTitle();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.reviewId = comment.getReview().getId();
    }


    public SaveCommentDto(String title, String content, Long userId, Long reviewId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.reviewId = reviewId;
    }
}
