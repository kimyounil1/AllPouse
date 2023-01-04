package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.exception.ExceptionEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static com.perfume.allpouse.exception.ExceptionEnum.*;

@Data
public class BannerResponseDto {

    private Long id;

    private String image;

    private LocalDateTime createDateTime;


    @QueryProjection
    public BannerResponseDto(Long id, List<String> images, LocalDateTime createDateTime) {
        this.id = id;
        this.createDateTime = createDateTime;
        if (images != null) {
            this.image = images.get(0);
            } else {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }
}
