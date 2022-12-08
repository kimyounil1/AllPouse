package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SavePerfumeDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "perfume_id")
    private Long id;

    @ColumnDefault("0")
    private int hitCnt;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int price;

    // 향수를 User가 등록하게 할 것인지 -> 논의 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;


    @Builder.Default
    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL)
    private List<ReviewBoard> reviews = new ArrayList<>();


    //== 연관관계 메소드 ==//
    // 1. Brand
    public void addBrand(Brand brand){
        this.brand = brand;
        brand.getPerfumes().add(this);
    }


    //== 향수 내용 변경 ==//

    //1. Dto -> Entity 내용 변경
    // 변경가능항목 : subject, content, price, image_path
    public void changePerfume(SavePerfumeDto perfumeDto) {
        this.subject = perfumeDto.getSubject();
        this.content = perfumeDto.getContent();
        this.price = perfumeDto.getPrice();
    }
}
