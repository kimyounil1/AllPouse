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

    // 향수에 대한 리뷰들의 평균
    @Column(name = "perfume_score", precision=3, scale=2)
    private BigDecimal score;

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
    // 1. 향수 생성시 점수 초기화
    public void initializeScore() {
        this.score = BigDecimal.ZERO;
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

        System.out.println("reviews : " + reviews);

        int reviewSize = reviews.size();

        // pre_review_size
        BigDecimal preReviewSize = new BigDecimal(reviewSize - 1);

        // post_review_size
        BigDecimal postReviewSize = new BigDecimal(reviewSize);

        // pre_sum_of_scores
        BigDecimal preSumOfScore = preReviewSize.multiply(score);

        // Save avg_perfume_score
        this.score = preSumOfScore.add(new BigDecimal(reviewScore)).divide(postReviewSize);
    }

    //
}
