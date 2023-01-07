package com.perfume.allpouse.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PerfumerApplicationResponseDto {

    private Long id;

    private Long userId;

    private String userName;

    private String imagePath;

    private String text;

    private String isApproved;

    private LocalDateTime createDateTime;


    @QueryProjection
    public PerfumerApplicationResponseDto(Long id, Long userId, String userName, List<String> imagePath, String text, boolean isApproved, LocalDateTime createDateTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.imagePath = imagePath.get(0);
        this.text = text;
        this.createDateTime = createDateTime;

        if (isApproved) {
            this.isApproved = "승인";
        } else {
            this.isApproved = "미승인";
        }
    }
}
