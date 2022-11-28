package com.perfume.allpouse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;

    private String subject;

    private String content;

    private String perfumeName;

    private String brandName;

    private int hitCnt;

    private int recommendCnt;

    private List<String> images;

    private LocalDateTime createdDateTime;
}
