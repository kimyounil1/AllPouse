package com.perfume.allpouse.model.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReviewCommentDto {

    private ReviewResponseDto reviewDto;

    private List<CommentResponseDto> commentDto;
}