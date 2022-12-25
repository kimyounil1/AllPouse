package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Post;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@ToString
public class PostResponseDto {

    private Long id;

    private String title;

    private String content;

    private int hitCnt;

    private int recommendCnt;

    private Long userId;

    private String userName;

    private LocalDateTime createDateTime;


    // Post -> PostResponseDto
    public static PostResponseDto toDto(final Post post) {

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .hitCnt(post.getHitCnt())
                .recommendCnt(post.getRecommendCnt())
                .userId(post.getUser().getId())
                .userName(post.getUser().getUserName())
                .createDateTime(post.getCreateDateTime())
                .build();
    }


    // List<Post> -> List<PostResponseDto>
    public static List<PostResponseDto> toDtoList(final List<Post> posts) {

        return posts.stream()
                .map(PostResponseDto::toDto)
                .collect(Collectors.toList());
    }


}
