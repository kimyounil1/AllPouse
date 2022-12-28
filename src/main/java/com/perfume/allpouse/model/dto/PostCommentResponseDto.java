package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.PostComment;
import com.perfume.allpouse.data.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class PostCommentResponseDto {

    private Long id;

    private String content;

    private int recommendCnt;

    private Long postId;

    private Long userId;

    private String userName;

    private Long referCommentId;

    private LocalDateTime createDateTime;


    @QueryProjection
    public PostCommentResponseDto(Long id, String content, int recommendCnt, Long postId, Long userId, String userName, Long referCommentId, LocalDateTime createDateTime) {
        this.id = id;
        this.content = content;
        this.recommendCnt = recommendCnt;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.referCommentId = referCommentId;
        this.createDateTime = createDateTime;
    }

    // PostComment -> PostCommentResponseDto
    public static PostCommentResponseDto toDto(PostComment postComment) {

        User user = postComment.getUser();

        return PostCommentResponseDto.builder()
                .id(postComment.getId())
                .content(postComment.getContent())
                .recommendCnt(postComment.getRecommendCnt())
                .postId(postComment.getPost().getId())
                .userId(user.getId())
                .userName(user.getUserName())
                .referCommentId(postComment.getReferCommentId())
                .createDateTime((postComment.getCreateDateTime()))
                .build();
    }


    // List<PostComment> -> List<PostCommentResponseDto>
    public static List<PostCommentResponseDto> toDtoList(List<PostComment> postComments){
        return postComments.stream()
                .map(PostCommentResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
