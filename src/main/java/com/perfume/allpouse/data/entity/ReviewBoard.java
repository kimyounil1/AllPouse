package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SaveReviewDto;
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
public class ReviewBoard extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "review_id")
    private Long id;

    @ColumnDefault("0")
    private int hitCnt;

    @ColumnDefault("0")
    private int recommendCnt;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_board_id")
    private PerfumeBoard perfume;

    private String imagePath;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    //== 연관관계 메소드 ==//
    // 1. User
    public void setUser(User user){
        this.user = user;
        user.getReviews().add(this);
    }

    // 2. PerfumeBoard
    public void setPerfume(PerfumeBoard perfume) {
        this.perfume = perfume;
        perfume.getReviews().add(this);
    }


    //== 리뷰 내용 변경 =//
    // 변경가능항목 : subject, content, image_path
    public void changeReview(SaveReviewDto reviewDto) {
        this.subject = reviewDto.getSubject();
        this.content = reviewDto.getContent();
        this.imagePath = reviewDto.getImagePath();
    }
}
