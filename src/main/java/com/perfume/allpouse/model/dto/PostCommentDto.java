package com.perfume.allpouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostCommentDto {

    private PostResponseDto postDto;

    private List<PostCommentResponseDto> postCommentDto;

}
