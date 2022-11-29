package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private Long reviewId;

    private LocalDateTime createDateTime;


    // Comment -> CommentResponseDto
    public static CommentResponseDto toDto(final Comment comment) {

        return CommentResponseDto.builder()
                .id(comment.getId())
                .title(comment.getTitle())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .reviewId(comment.getReview().getId())
                .createDateTime(comment.getCreateDateTime())
                .build();
    }
}
