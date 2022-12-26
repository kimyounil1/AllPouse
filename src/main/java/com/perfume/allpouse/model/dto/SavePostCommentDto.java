package com.perfume.allpouse.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SavePostCommentDto {

    private Long id;

    private String content;

    private Long userId;

    private String userName;

    private Long postId;

    private Long referCommentId;

    private int recommendCnt;

    private LocalDateTime createdDateTime;
}



