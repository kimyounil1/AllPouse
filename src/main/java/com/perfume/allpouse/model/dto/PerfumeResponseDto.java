package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PerfumeResponseDto {

    private Long id;

    private String subject;

    private String content;

    private int price;

    private int hitCnt;

    private int reviewCnt;

    private String brandName;

    private LocalDateTime createDateTime;


    // PerfumeBoard -> ResponseDto
    public static PerfumeResponseDto toDto(PerfumeBoard perfumeBoard) {

        return PerfumeResponseDto.builder()
                .id(perfumeBoard.getId())
                .subject(perfumeBoard.getSubject())
                .content(perfumeBoard.getContent())
                .price(perfumeBoard.getPrice())
                .hitCnt(perfumeBoard.getHitCnt())
                .reviewCnt(perfumeBoard.getReviews().size())
                .brandName(perfumeBoard.getBrand().getName())
                .createDateTime(perfumeBoard.getCreateDateTime())
                .build();
    }
}
