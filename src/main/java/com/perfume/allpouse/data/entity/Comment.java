package com.perfume.allpouse.data.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    private ReviewBoard review;


    //== 연관관계 메소드==//
    // 1. User
    public void setUser(User user){
        this.user = user;
        user.getComments().add(this);
    }

    // 2. Review
    public void setReview(ReviewBoard review){
        this.review = review;
        review.getComments().add(this);
    }

}
