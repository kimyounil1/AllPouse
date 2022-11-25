package com.perfume.allpouse.model.dto;

import com.perfume.allpouse.data.entity.Brand;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveBrandDto {

    private Long id;

    private String name;

    private String content;


    // Brand -> Dto
    public SaveBrandDto(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public SaveBrandDto(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.content = brand.getContent();
    }
}
