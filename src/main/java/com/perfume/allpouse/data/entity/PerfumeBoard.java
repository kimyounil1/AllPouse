package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.utils.StringListConverter;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
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

    // 향수들의 리뷰에 대한 평균
    @ColumnDefault("0")
    @Column(name = "perfume_score")
    private double score;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewBoard> reviews = new ArrayList<>();

    //== 연관관계 메소드 ==//
    // 1. Brand
    public void addBrand(Brand brand) {
        this.brand = brand;
        brand.getPerfumes().add(this);
    }

    // 2. Dto -> Entity 내용 변경
    // 변경가능항목 : subject, content, price, image_path
    public void changePerfume(SavePerfumeDto perfumeDto) {
        this.subject = perfumeDto.getSubject();
        this.content = perfumeDto.getContent();
        this.price = perfumeDto.getPrice();
    }

    // 3. ReviewScore -> PerfumeScore 반영
    public void convertToPerfumeScore(int reviewScore) {
        int reviewSize = reviews.size();
        int preReviewSize = reviewSize - 1;

        double preTotal = score * preReviewSize;

        double renewedScore = Math.round(((preTotal + reviewScore) / reviewSize) * 100) / 100.0;

        this.score = renewedScore;
    }
}