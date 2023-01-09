package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.utils.StringListConverter;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    // 향수에 대한 점수(리뷰의 점수 합 / 리뷰 수)
    @ElementCollection
    @MapKeyColumn(name = "attribute")
    @Column(name = "value")
    @CollectionTable(name = "score_attributes", joinColumns = @JoinColumn(name = "score_id"))
    Map<String, Long> score = new HashMap<>(){{
        put("persistence", 0L); // 지속성
        put("costEffectiveness", 0L); // 가성비
        put("attribute3", 0L); // 속성3
        put("attribute4", 0L); // 속성4
        put("attribute5", 0L); // 속성5
    }};


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
