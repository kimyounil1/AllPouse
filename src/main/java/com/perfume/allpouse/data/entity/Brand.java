package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.service.dto.SaveBrandDto;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brand {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "brand_id")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imagePath;

    @Builder.Default
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<PerfumeBoard> perfumes = new ArrayList<>();


    //== Brand 수정 ==//
    // 변경 가능항목 : name, content, image_path
    public void changeBrand(SaveBrandDto brandDto) {
        this.name = brandDto.getName();
        this.content = brandDto.getContent();
        this.imagePath = brandDto.getImagePath();
    }
}
