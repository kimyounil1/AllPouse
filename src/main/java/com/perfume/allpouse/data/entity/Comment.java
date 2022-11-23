package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SaveCommentDto;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


    //== 연관관계 메소드 ==//
    // 1. User
    public void setUser(User user) {
        user.getComments().add(this);
        this.user = user;
    }

    // 2. Review
    public void setReview(ReviewBoard reviewBoard) {
        this.review = reviewBoard;
        reviewBoard.getComments().add(this);
    }


    //== 댓글 내용 변경 ==//
    // 변경가능항목 : title, content
    public void changeComment(SaveCommentDto commentDto) {

        this.title = commentDto.getTitle();
        this.content = commentDto.getContent();
    }
}