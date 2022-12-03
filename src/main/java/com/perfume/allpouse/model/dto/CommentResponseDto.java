package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@ToString
@Getter
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private String userName;

    private Long reviewId;

    private String perfumeName;

    private LocalDateTime createDateTime;


    // Comment -> CommentResponseDto
    public static CommentResponseDto toDto(final Comment comment) {

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
    public static List<CommentResponseDto> toDtoList(final List<Comment> comments) {

        return comments.stream()
                .map(CommentResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
