package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SavePerfumeDto {

    private Long id;

    private String subject;

    private String content;

    private int price;

    private Long brandId;

    private String imagePath;


    // PerfumeBoard -> Dto
    public SavePerfumeDto(PerfumeBoard perfumeBoard) {
        this.id = perfumeBoard.getId();
        this.subject = perfumeBoard.getSubject();
        this.content = perfumeBoard.getContent();
        this.price = perfumeBoard.getPrice();
        this.brandId = perfumeBoard.getBrand().getId();
        this.imagePath = perfumeBoard.getImagePath();
    }


    public SavePerfumeDto(String subject, String content, int price, Long brandId, String imagePath) {
        this.subject = subject;
        this.content = content;
        this.price = price;
        this.brandId = brandId;
        this.imagePath = imagePath;
    }
}