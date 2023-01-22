package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Comment;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@ToString
@Data
public class CommentResponseDto {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private String userName;

    private List<String> userImage;

    private Long reviewId;

    private String perfumeName;

    private LocalDateTime createDateTime;


    @QueryProjection
    public CommentResponseDto(Long id, String title, String content, Long userId, String userName, List<String> userImage, Long reviewId, String perfumeName, LocalDateTime createDateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.reviewId = reviewId;
        this.perfumeName = perfumeName;
        this.createDateTime = createDateTime;
    }


    // Comment -> CommentResponseDto
    public static CommentResponseDto toDto(Comment comment) {

        return CommentResponseDto.builder()
                .id(comment.getId())
                .title(comment.getTitle())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getUserName())
                .reviewId(comment.getReview().getId())
                .perfumeName(comment.getReview().getPerfume().getSubject())
                .createDateTime(comment.getCreateDateTime())
                .build();
    }


    // List<Comment> -> List<CommentResponseDto>
    public static List<CommentResponseDto> toDtoList(List<Comment> comments) {

        return comments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
