package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import lombok.*;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SavePerfumeDto {

    private Long id;

    private String subject;

    private String content;

    private int price;

    private Long brandId;



    // PerfumeBoard -> Dto
    public SavePerfumeDto(PerfumeBoard perfumeBoard) {
        this.id = perfumeBoard.getId();
        this.subject = perfumeBoard.getSubject();
        this.content = perfumeBoard.getContent();
        this.price = perfumeBoard.getPrice();
        this.brandId = perfumeBoard.getBrand().getId();
    }


    public SavePerfumeDto(String subject, String content, int price, Long brandId) {
        this.subject = subject;
        this.content = content;
        this.price = price;
        this.brandId = brandId;
    }
}