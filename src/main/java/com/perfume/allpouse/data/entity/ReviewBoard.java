package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.utils.LongListConverter;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewBoard extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "review_id")
    private Long id;

    @ColumnDefault("0")
    private int hitCnt;

    @ColumnDefault("0")
    private int recommendCnt;

    @Convert(converter = LongListConverter.class)
    @Builder.Default
    private List<Long> recommendUserList = new ArrayList<>();

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 각 리뷰의, 향수에 대한 점수
    @Column(name = "review_score")
    private Integer score;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "perfume_board_id")
    private PerfumeBoard perfume;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    //== 연관관계 메소드 ==//
    // 1. User
    public void setUser(User user) {
        this.user = user;
        user.getReviews().add(this);
    }

    // 2. PerfumeBoard
    public void setPerfume(PerfumeBoard perfume) {
        this.perfume = perfume;
        perfume.getReviews().add(this);
    }


    //== 리뷰 내용 변경 =//
    // 1. Dto -> Entity 내용 변경
    // 변경가능항목 : subject, content
    public void changeReview(SaveReviewDto reviewDto) {
        this.subject = reviewDto.getSubject();
        this.content = reviewDto.getContent();
    }
}
