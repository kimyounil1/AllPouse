package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.utils.StringListConverter;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.FetchType.LAZY;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Convert(converter = StringListConverter.class)
    @Builder.Default
    private List<String> keyword = new ArrayList<>();

    @ElementCollection(fetch = LAZY)
    @CollectionTable(
            name="perfume_score",
            joinColumns =@JoinColumn(name="perfume_id")
    )
    @MapKeyColumn(name="attribute")
    @Column(name="value")
    @Builder.Default
    Map<String, Long> perfumeScore = new HashMap<>(){{
        /*
         리뷰 점수 초기화
         */
        // 지속성
        put("persistence", 0L);
        // 잔향
        put("sillage", 0L);
        // 확산력
        put("diffusion", 0L);
        // 가성비
        put("cost_effectiveness", 0L);
        // 디자인(향수병)
        put("design", 0L);
    }};


    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewBoard> reviews = new ArrayList<>();


    //== 연관관계 메소드 ==//
    // 1. Brand
    public void addBrand(Brand brand){
        this.brand = brand;
        brand.getPerfumes().add(this);
    }


    //== 향수 내용 변경 ==//

    // 1. Dto -> Entity 내용 변경
    // 변경가능항목 : subject, content, price, image_path
    public void changePerfume(SavePerfumeDto perfumeDto) {
        this.subject = perfumeDto.getSubject();
        this.content = perfumeDto.getContent();
        this.price = perfumeDto.getPrice();
    }

    // 2. Perfume_Score <- Review_Score 반영
    public void updatePerfumeScore(Map<String, Long> reviewScore) {

        int size = this.reviews.size() + 1;
        Set<String> reviewKeySet = reviewScore.keySet();
        Set<String> perfumeKeySet = this.perfumeScore.keySet();

        for (String attribute : perfumeKeySet) {

            Long reviewValue = reviewScore.get(attribute);
            this.perfumeScore.put(attribute,
                    (this.perfumeScore.get(attribute) + reviewValue) / size);
        }
    }
}
