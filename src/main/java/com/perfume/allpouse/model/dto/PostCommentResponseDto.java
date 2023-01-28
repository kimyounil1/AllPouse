package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostCommentResponseDto {

    private Long id;

    private String content;

    private int recommendCnt;

    private Long postId;

    private Long userId;

    private String userName;

    private List<String> userImage;

    private Long referCommentId;

    private boolean isRecommended;

    private LocalDateTime createDateTime;


    @QueryProjection
    public PostCommentResponseDto(Long id, String content, int recommendCnt, Long postId, Long userId, String userName, List<String> userImage, Long referCommentId, LocalDateTime createDateTime) {
        this.id = id;
        this.content = content;
        this.recommendCnt = recommendCnt;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.referCommentId = referCommentId;
        this.createDateTime = createDateTime;
    }

}
