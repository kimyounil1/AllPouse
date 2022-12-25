package com.perfume.allpouse.data.entity;

import com.perfume.allpouse.model.dto.SavePostCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class PostComment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = AUTO)
    @Column(name = "post_comment_id")
    private Long id;

    private String content;

    @ColumnDefault("0")
    private int recommendCnt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long referCommentId;

    //== 연관관계 편의 메소드 ==//
    // 1. Post
    public void setPost(Post post) {
        this.post = post;
        post.getPostComments().add(this);
    }

    // 2. User
    public void setUser(User user) {
        this.user = user;
        user.getPostComments().add(this);
    }


    //== 댓글 내용 변경==//
    // 변경가능항목 : content
    public void changeComment(SavePostCommentDto postCommentDto) {
        this.content = postCommentDto.getContent();
    }
}
