package com.perfume.allpouse.service.dto;

import com.perfume.allpouse.data.entity.Brand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveBrandDto {

    private Long id;

    private String name;

    private String content;

    private String imagePath;


    // Brand -> Dto
    public SaveBrandDto(String name, String content, String imagePath) {
        this.name = name;
        this.content = content;
        this.imagePath = imagePath;
    }

    public SaveBrandDto(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.content = brand.getContent();
        this.imagePath = brand.getImagePath();
    }
}
