package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SaveBrandDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "brand_id")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ColumnDefault("0")
    private int hitCnt;

    @Builder.Default
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<PerfumeBoard> perfumes = new ArrayList<>();


    //== Brand 수정 ==//
    // 변경 가능항목 : name, content, image_path
    public void changeBrand(SaveBrandDto brandDto) {
        this.name = brandDto.getName();
        this.content = brandDto.getContent();
    }
}
