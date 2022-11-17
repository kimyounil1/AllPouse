package com.perfume.allpouse.data.entity;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "perfume_id")
    private Long id;

    private int hitCnt;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int price;

    // 향수를 User가 등록하게 할 것인지 -> 논의 필요


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private String imagePath;

    @Builder.Default
    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL)
    private List<ReviewBoard> reviews = new ArrayList<>();


    //== 연관관계 메소드 ==//
    // 1. Brand
    public void addBrand(Brand brand){
        this.brand = brand;
        brand.getPerfumes().add(this);
    }

}
